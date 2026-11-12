param(
    [string]$SdkPlatform = (Join-Path $PSScriptRoot '..\sdk-platform\android-36'),
    [string]$BuildTools = (Join-Path $PSScriptRoot '..\sdk-build\android-16'),
    [string]$JeroMqJar = (Join-Path $PSScriptRoot 'libs\jeromq-0.6.0.jar'),
    [string]$Contact = '',
    [string]$Output = (Join-Path $PSScriptRoot 'build\SlimOV.apk')
)
$ErrorActionPreference = 'Stop'
if (-not $Contact) { throw 'Supply -Contact with a real project contact address or project website (Transitous policy).' }
$build = Join-Path $PSScriptRoot 'build'
$classes = Join-Path $build 'classes'
$dex = Join-Path $build 'dex'
$generated = Join-Path $build 'generated\nl\slimov\app'
New-Item -ItemType Directory -Force -Path $build,$classes,$dex,$generated | Out-Null
$androidJar = Join-Path $SdkPlatform 'android.jar'
$contactLiteral = $Contact.Replace('\','\\').Replace('"','\"')
Set-Content -LiteralPath (Join-Path $generated 'BuildConfig.java') -Encoding utf8 -Value ('package nl.slimov.app; public final class BuildConfig { public static final String CONTACT="' + $contactLiteral + '"; }')
function Run-Checked([string]$Program, [string[]]$Arguments) {
    & $Program @Arguments
    if ($LASTEXITCODE -ne 0) { throw "$Program failed with exit code $LASTEXITCODE" }
}
Run-Checked (Join-Path $BuildTools 'aapt2.exe') @('compile','--dir',(Join-Path $PSScriptRoot 'res'),'-o',(Join-Path $build 'resources.zip'))
Run-Checked (Join-Path $BuildTools 'aapt2.exe') @('link','-I',$androidJar,'--manifest',(Join-Path $PSScriptRoot 'AndroidManifest.xml'),'-A',(Join-Path $PSScriptRoot 'assets'),'-o',(Join-Path $build 'unsigned.apk'),(Join-Path $build 'resources.zip'))
$javaSources = @((Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'java') -Filter '*.java' -Recurse).FullName) + (Join-Path $generated 'BuildConfig.java')
Run-Checked 'javac' (@('--release','8','-encoding','UTF-8','-classpath',"$androidJar;$JeroMqJar",'-d',$classes) + $javaSources)
Add-Type -AssemblyName System.IO.Compression.FileSystem
$classJar = Join-Path $build 'classes.jar'
if (Test-Path -LiteralPath $classJar) { Remove-Item -LiteralPath $classJar }
[IO.Compression.ZipFile]::CreateFromDirectory($classes,$classJar)
Run-Checked (Join-Path $BuildTools 'd8.bat') @('--lib',$androidJar,'--min-api','31','--output',$dex,(Join-Path $build 'classes.jar'),$JeroMqJar)
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [IO.Compression.ZipFile]::Open((Join-Path $build 'unsigned.apk'),[IO.Compression.ZipArchiveMode]::Update)
try { foreach ($file in Get-ChildItem -LiteralPath $dex -Filter '*.dex') { $old=$zip.GetEntry($file.Name); if ($old) { $old.Delete() }; [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip,$file.FullName,$file.Name,[IO.Compression.CompressionLevel]::Optimal) | Out-Null } } finally { $zip.Dispose() }
Run-Checked (Join-Path $BuildTools 'zipalign.exe') @('-f','-p','4',(Join-Path $build 'unsigned.apk'),(Join-Path $build 'aligned.apk'))
$private = Join-Path $PSScriptRoot '..\private'
New-Item -ItemType Directory -Force -Path $private | Out-Null
$key = Join-Path $private 'slimov-signing.p12'
$passwordFile = Join-Path $private 'signing-password.txt'
if (-not (Test-Path -LiteralPath $key)) {
    $password=[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(24))
    Set-Content -LiteralPath $passwordFile -Value $password -NoNewline
    $env:SLIMOV_KEYPASS=$password
    Run-Checked 'keytool' @('-genkeypair','-keystore',$key,'-storetype','PKCS12','-storepass:env','SLIMOV_KEYPASS','-keypass:env','SLIMOV_KEYPASS','-alias','slimov','-keyalg','RSA','-keysize','3072','-validity','10000','-dname','CN=SlimOV Personal, O=SlimOV, C=NL')
} else { $env:SLIMOV_KEYPASS = Get-Content -LiteralPath $passwordFile -Raw }
New-Item -ItemType Directory -Force -Path (Split-Path -Parent $Output) | Out-Null
Run-Checked (Join-Path $BuildTools 'apksigner.bat') @('sign','--ks',$key,'--ks-key-alias','slimov','--ks-pass','env:SLIMOV_KEYPASS','--key-pass','env:SLIMOV_KEYPASS','--out',$Output,(Join-Path $build 'aligned.apk'))
Run-Checked (Join-Path $BuildTools 'apksigner.bat') @('verify','--verbose',$Output)
Run-Checked (Join-Path $BuildTools 'zipalign.exe') @('-c','4',$Output)
$env:SLIMOV_KEYPASS=$null
Get-Item -LiteralPath $Output | Select-Object FullName,Length
