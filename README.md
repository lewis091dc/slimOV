# SlimOV

## Testversie 0.4.1

NS-reparatie: vanuit een trein op de kaart zijn samenstelling en Rit & haltes bereikbaar. De officiële NS-definities zijn gecontroleerd; live antwoorden met de lokale sleutel moeten op de telefoon worden getest. Zie [NS-testgids](docs/testgids-0.4.1.md).

Ronde 2 met ideeën 21–40: eigen grote kaartpagina, voertuigtekeningen, duidelijke haltebezoeken, lokale ritgeschiedenis, favoriete vertrekborden, aankomsten, betere verstoringen en optionele Android-achtergrondbewaking. NS-posities en treinstellen hebben een optionele lokale API-sleutel nodig en zijn nog niet met die sleutel getest. Zie [testgids en exacte functiestatus](docs/testgids-0.4.0.md). Nog geen V1; ronde 41–60 wacht op gebruikersfeedback.

Tests: `node --test tests/*.test.cjs` (74 tests); daarnaast 12 decodercontroles en 10 JVM-controles van bewakingslogica. Android-toestemmingen en achtergrondgedrag moeten nog op het echte toestel worden getest.

Persoonlijke Android-app voor Nederlands openbaar vervoer. Het ontwerp gebruikt paarse accenten en rustige lichte/donkere kaarten, geïnspireerd op de door de gebruiker aangeleverde SlimLearn-screenshots. Dit project is onafhankelijk van SlimLearn, OVinfo, 9292 en vervoerders.

## Nieuw in 0.2.0

Bij **Volg live** worden routegeometrie en tussenhaltes automatisch geladen. De kaart toont genummerde haltes en benadrukt de halte bij de bus en de volgende halte. De camera houdt het voertuig en de aangrenzende haltes samen in beeld. Verslepen pauzeert het centreren; **Centreer bus** schakelt het weer in. **Overzicht** toont de hele route.

Een kaartje benoemt de vorige/volgende halte en de leeftijd van de voertuigpositie. Het gevolgde traject is grijs, het resterende traject paars, wanneer de positie betrouwbaar op de bekende route kan worden geplaatst. **Alle haltes** opent een lijst met beschikbare perrons/tijden en voortgang, direct bij de bus. Een lokale berekening en korte routecache beperken extra dataverbruik.

Dit is een afleiding uit de gemelde positie. Bij posities ouder dan twee minuten, meer dan ongeveer 180 meter naast de route, ontbrekende geometrie of onvoldoende data wordt geen volgende halte als zeker getoond. “Voorbij volgens positie” bevestigt niet dat de bus daadwerkelijk is gestopt. Een schematische verbinding tussen haltes wordt expliciet als schematisch aangeduid.

## Wat werkt

- Reisadvies van halte, station, adres of huidige locatie naar een bestemming, met vertrek-/aankomsttijd, overstappen en routegeometrie via Transitous/MOTIS.
- Kaart met openbare voertuigposities en aanklikbare haltes. Voertuigen worden bij uitzoomen gegroepeerd.
- Rechtstreekse KV6-liveverbinding met één vervoerdersgroep; geen eigen backend of abonnement nodig.
- Gepland/actueel vertrekbord met vervoerder, lijn, bestemming, perron en filters op lijn, perron en richting.
- Rood bij minimaal 30 seconden vertraging, blauw bij minimaal 30 seconden voorsprong, groen bij minder dan 30 seconden afwijking. Onbekende/verouderde data zijn grijs.
- Zoeken in actuele omleidingen en verstoringen uit de openbare GTFS-Realtime-feed.
- Lokale favorieten en een eigen aanvulbaar voertuigregister.
- Officiële voertuigregistraties voor 3.847 bussen van CXX, EBS en Qbuzz; 3.817 RDW-koppelingen. Het register is een momentopname van 12 november 2026.
- Systeemthema, lichte en donkere weergave; optionele locatie; Android 16-backnavigatie en scherm-/toetsenbordinsets.

## Grenzen van deze versie

- Dit is versie 0.3.0, een persoonlijke testversie. De APK is gebouwd en de ondertekening gecontroleerd, maar is nog niet op het fysieke OnePlus-toestel getest. Dezelfde ondertekening als 0.1.0 en een hogere versionCode maken een normale update mogelijk.
- De broncode is openbaar gepubliceerd op https://github.com/lewis091dc/slimOV voor de Transitous-integratie. Zie hun voorwaarden: https://transitous.org/api/ . Publicatie vereist geen betaalde dienst.
- Een dataleeftijd onder 30 seconden is niet gegarandeerd. Het landelijke voertuigbestand verandert ongeveer eens per minuut. De rechtstreekse KV6-feed kan sneller zijn, maar de vervoerder bepaalt wanneer een melding verschijnt. De app toont de leeftijd per voertuig.
- De landelijke voertuigfeed is hoofdzakelijk bus/tram/metro. Een volledige NS-treinenkaart, actuele treinstelnummers en treinmaterieel zijn niet gekoppeld in deze versie. Treinreisadviezen en treinvertrektijden werken via de reisplannerbron.
- Niet elk busmerk/model is bekend. Alleen een bevestigde voertuigkoppeling of expliciete eigen invoer levert type-informatie op. Een NeTEx-typeomschrijving kan algemener zijn dan een exact commercieel model.
- Omleidingen worden als berichten weergegeven; de exacte afwijkende lijn op de kaart wordt niet afgeleid uit alleen tekst.
- Meegeleverde haltes, routes en voertuigregisters worden met een nieuwe build bijgewerkt. De vertrekbordaanvraag gebruikt ook coördinaten als terugval bij veranderde halte-ID's.
- Gratis bronnen draaien op best-effortbasis en kunnen onbereikbaar zijn of hun voorwaarden wijzigen. SlimOV bevat geen betaalde fallback, proefabonnement of betaalgegevens.

## Dataverbruik

Haltes, routes en voertuigregister zijn meegeleverd. Kaarttegels worden met Androids HTTP-cache lokaal hergebruikt; geen bulkdownloads of prefetch.

Standaard haalt het kaartscherm het landelijke voertuigbestand hoogstens eens per minuut op, met ETag/If-None-Match. Een nieuwe volledige feed mat tijdens ontwikkeling ongeveer 0,33 MB. Dat is ongeveer 20 MB per uur bij voortdurend geopende kaart en iedere minuut een nieuwe feed, exclusief kaarttegels/netwerkoverhead.

Bij een actieve directe livefeed wordt het landelijke overzicht in de zuinige stand hoogstens eens per vier minuten vernieuwd. De directe feed ontvangt alle berichten voor de gekozen vervoerdersgroep. Tijdens een korte GVB-test kwam ongeveer 12 kB in twaalf seconden binnen; dat is een meting, geen garantie voor andere tijden/vervoerders. Filteren op één voertuig beperkt de gedownloade groepsfeed niet.

Liveverbindingen stoppen wanneer de Activity pauzeert. Vertrekborden verversen eens per 30 seconden wanneer het halteblad openstaat; reisadvies wordt alleen op aanvraag berekend. Instellingen tonen Androids ontvangst/verzendtellers voor de app sinds de toestelstart.

## Zelf bouwen op Windows

Benodigd: Node.js 20+, JDK 21, officiële Android-platformbestanden voor API 36 en Android Build Tools 36.0.0. In de complete broncode-ZIP zijn data en bibliotheken meegeleverd. Een verse Git-checkout haalt de openbare data en bibliotheken eenmalig op met `node tools/restore.mjs` (circa 170 MB voor de landelijke GTFS; deze download gebeurt bij het bouwen, niet op de telefoon).

```powershell
./build.ps1 -SdkPlatform 'C:\Android\Sdk\platforms\android-36' `
  -BuildTools 'C:\Android\Sdk\build-tools\36.0.0' `
  -Contact 'https://github.com/JOUW-ACCOUNT/SlimOV' `
  -Output 'C:\build\SlimOV.apk'
```

`-Contact` moet een bestaand projectadres of projectwebsite zijn. De toolchain compileert Java, maakt DEX, verpakt assets, lijnt de APK uit en ondertekent hem met APK Signature Scheme v3. De eerste build maakt een persoonlijke sleutel in de naastliggende map `private/`. Bewaar die sleutel en het wachtwoord veilig voor updates; publiceer ze niet. Deze map en de gegenereerde contactconfiguratie zijn niet opgenomen in de broncodelevering.

Voor de lokale browserpreview (geen onderdeel van de APK):

```powershell
$env:SLIMOV_CONTACT='https://github.com/JOUW-ACCOUNT/SlimOV'
node tools/dev-server.mjs
# Open http://127.0.0.1:5178
```

De browserpreview test echte reisadviezen, vertrekken en kaartdata. De ZeroMQ-feed en Android-lifecycle zijn native Java en worden afzonderlijk getest. Er is geen server nodig om de APK te gebruiken.

## Verificatie

Zie `tests/DecoderTest.java` en `tests/StreamIntegration.java`. De JVM-adapter onder `tests/android/` is uitsluitend een testadapter en wordt nooit in de APK opgenomen. De decodertests controleren echte protobuf-feeds, negatieve vertraging, ongeldige lengtes, gzip/namespaced KV6, voertuignummers en RD→WGS84. De integratietest ontvangt echte GVB-posities en controleert dat ontvangen stopt na het sluiten van de verbinding.

`node --test tests/route-model.test.cjs` controleert de afleiding van volgende haltes, huidige/eindhalte, ontbrekende/verouderde/afwijkende posities, vervallen haltes, ringlijnen, routekruisingen en het splitsen van de geometrie. De tussenhaltes en routegeometrie komen van dezelfde rit, niet van de geografisch dichtstbijzijnde willekeurige halte.

De mobiele browsercontrole omvat haltezoeken, favoriet opslaan, filteren op lijn/perron, reisadvies Utrecht–Amsterdam, reisdetails en routeweergave, meldingen doorzoeken en lichte/donkere weergave. Dat is geen vervanging voor een test op een echte Android-telefoon.

## Bronnen en privacy

Geen analytics, advertenties of gebruikersaccount. Favorieten, instellingen en eigen voertuiggegevens blijven lokaal. Transitous ontvangt aangevraagde reislocaties/adreszoekwoorden. OpenStreetMap ontvangt het bekeken kaartgebied. Diensten zien IP-adres en de technische app-identificatie. De gebruiker geeft locatietoestemming alleen op eigen verzoek.

- Transitous API en verplichte bronvermelding: https://transitous.org/api/ en https://transitous.org/sources/
- Nederlandse GTFS/GTFS-RT: https://gtfs.ovapi.nl/nl/
- NDOV/GOVI KV6: https://govi.nu/realtime.html
- Voertuigregisters: de bron-URL's staan in `assets/fleet.js` onder `FLEET_SOURCES`.
- RDW: https://opendata.rdw.nl/Voertuigen/Gekentekende_voertuigen/m9d7-ebf2
- OpenStreetMap: https://www.openstreetmap.org/copyright

Eigen code: MIT. Meegeleverde bibliotheken en data behouden hun eigen voorwaarden. Zie `NOTICE.md` en `assets/licenses/`.
