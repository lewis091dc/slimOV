// Restore public dependencies and generated data for a fresh Git checkout.
// Run from the repository root. No account, token, or private service needed.
import fs from 'node:fs';import zlib from 'node:zlib';import {execFileSync} from 'node:child_process';
const headers={'User-Agent':'SlimOV-source-build/0.1.0 (https://github.com/lewis091dc/slimOV)','Accept-Encoding':'gzip'};
async function download(url,file){fs.mkdirSync(file.slice(0,file.lastIndexOf('/')),{recursive:true});let r=await fetch(url,{headers,signal:AbortSignal.timeout(180000)});if(!r.ok)throw Error(url+': HTTP '+r.status);fs.writeFileSync(file,Buffer.from(await r.arrayBuffer()));return r;}
await Promise.all([
 download('https://unpkg.com/leaflet@1.9.4/dist/leaflet.js','assets/leaflet.js'),
 download('https://unpkg.com/leaflet@1.9.4/dist/leaflet.css','assets/leaflet.css'),
 download('https://repo.maven.apache.org/maven2/org/zeromq/jeromq/0.6.0/jeromq-0.6.0.jar','libs/jeromq-0.6.0.jar')
]);
console.log('Downloading the national GTFS archive for build-time catalog preparation…');
let r=await download('https://gtfs.ovapi.nl/nl/gtfs-nl.zip','build/data/gtfs-nl.zip');fs.writeFileSync('build/data/date.txt',new Date(r.headers.get('last-modified')||Date.now()).toISOString().slice(0,10));
let b=fs.readFileSync('build/data/gtfs-nl.zip'),p=b.length-22;while(p>=0&&b.readUInt32LE(p)!==0x06054b50)p--;if(p<0)throw Error('Invalid ZIP directory');p=b.readUInt32LE(p+16);
while(b.readUInt32LE(p)===0x02014b50){let n=b.readUInt16LE(p+28),x=b.readUInt16LE(p+30),c=b.readUInt16LE(p+32),name=b.subarray(p+46,p+46+n).toString();if(['agency.txt','routes.txt','stops.txt'].includes(name)){let q=b.readUInt32LE(p+42),size=b.readUInt32LE(p+20),offset=q+30+b.readUInt16LE(q+26)+b.readUInt16LE(q+28);let raw=b.subarray(offset,offset+size),out=b.readUInt16LE(q+8)===0?raw:zlib.inflateRawSync(raw);fs.writeFileSync('build/data/'+name,out);}p+=46+n+x+c;}
execFileSync(process.execPath,['tools/catalog.mjs'],{stdio:'inherit'});
const sources=[];for(let owner of ['cxx','ebs','qbuzz']){let base='https://data.ndovloket.nl/netex/'+owner+'/',html=await (await fetch(base,{headers})).text(),files=[...html.matchAll(/class="link" href="([^"]*vehicles[^\"]*\.gz)"/gi)].map(m=>m[1]).sort();if(!files.length)continue;let file=files.at(-1),local=owner+'-'+file;await download(base+file,'build/fleet-input/'+local);sources.push({owner,file:local,url:base+file});}
fs.mkdirSync('build/fleet-input',{recursive:true});fs.writeFileSync('build/fleet-input/sources.json',JSON.stringify(sources));execFileSync(process.execPath,['tools/fleet.mjs','build/fleet-input','--rdw'],{stdio:'inherit'});console.log('Public dependencies and generated data restored. Run build.ps1 with Android SDK paths and a real project contact.');
