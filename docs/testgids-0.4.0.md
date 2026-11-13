# SlimOV 0.4.0 — testversie, ronde 2

Deze ronde bevat de uitbreidingen 21–40 en de kaart-, rit- en haltefeedback. Dit is nog geen V1. Ronde 41–60 volgt na jouw feedback. Installeer de APK over de bestaande app; verwijder de vorige versie niet als je de lokale favorieten wilt behouden. Pakketnaam en ondertekening zijn behouden.

## Treinlocaties: jouw vraag

De oorspronkelijke openbare voertuigfeed levert vooral bus-, tram- en metroposities. Een traject en de tekst tussen haltes kunnen uit de dienstregeling of een routeberekening komen: dat bewijst geen actuele trein-GPS.

Ga in de Android-app naar **Instellingen → NS API & treinen**. Vul daar je eigen sleutel in voor het NS-product met Virtual Train API. De sleutel wordt met Android Keystore versleuteld opgeslagen en alleen als HTTPS-header aan gateway.apiportal.ns.nl verstuurd. Hij staat niet in exports, broncode of de APK. Verwijderen kan in hetzelfde scherm. Een verkeerd API-product of ontbrekend abonnement kan een 401/403 opleveren. Deel de sleutel niet in de chat.

De treinfilter verschijnt pas wanneer de ingestelde koppeling daadwerkelijk treinposities teruggeeft. Zonder meettijd staat de ophaaltijd bij een treinpositie; zo'n positie telt niet als betrouwbare GPS-voortgang. Actuele treinsamenstelling wordt afzonderlijk voor het geselecteerde ritnummer en tijdstip opgevraagd. Een ritnummer is geen treinstelnummer.

**Deze NS-koppeling is gebouwd maar niet met jouw sleutel getest.** De adapter ondersteunt defensief bekende antwoordvelden. Werkelijke dekking en jouw API-product moeten op de telefoon worden bevestigd. Arriva, Blauwnet en andere vervoerders hebben niet automatisch dezelfde positie- en materieeldekking. Ontbrekend merk, type, treinstelnummer of aantal bakken blijft onbekend. SlimOV leidt dat niet af uit alleen de vervoerder of het label Intercity/Sprinter.

## Haltes en voortgang

- **✓ Bezocht:** geregistreerde aankomst/vertrek met bronreden OBSERVED, of expliciet ontvangen stilstand bij de exacte halte. Het scherm benoemt een stilstandmeting apart; dit is geen officiële aankomsttijd.
- **↗ Gepasseerd volgens GPS:** het voertuig is op het bekende traject voorbij de halte. Stoppen en het exacte tijdstip zijn niet bevestigd.
- **● Bij de halte:** geografische nabijheid; zonder exacte stilstandmelding geen bevestiging dat er is gehalteerd.
- **Nog te gaan:** volgt nog in de betrouwbare GPS-routevolgorde.
- **Bezoek niet bevestigd:** onvoldoende actuele gegevens. Een geplande of voorspelde tijd markeert nooit automatisch een bezoek.
- **× Halte vervalt:** bron meldt een vervallen ritstop; dit overschrijft de gewone voortgang.

Dit geldt voor bus, trein, tram en metro. De ritlijst begint standaard bij de laatste relevante halte. **Bekijk vorige haltes** opent de eerdere haltes. Het bewegende voertuigje en de contextregel tonen waar mogelijk het traject tussen twee haltes, stilstand op een bevestigde halte en aankomst bij het eindpunt.

De ritpagina heeft een blijvend bereikbare **Routekaart**-knop. Het kleinere routevoorbeeld staat gecentreerd. De kaart heeft een eigen grote pagina; de verversknop staat direct onder de locatieknop. Het kaartje bij een individuele rit blijft als snelkoppeling bestaan.

## Ideeën 21–40

| Nr. | Uitwerking in deze testversie |
|---|---|
| 21 | ‘Wat rijdt hier?’ groepeert ontvangen voertuigen in het zichtbare kaartgebied. Geen garantie dat alle rijdende voertuigen GPS leveren. |
| 22 | Korte vloeiende overgang tussen ontvangen meetpunten. ≈ markeert de geschatte tussenpositie; geen vooruitberekende toekomstige GPS. |
| 23 | Rijrichting uit bruikbare bronrichting of voldoende gemeten verplaatsing. Stilstand en kleine GPS-sprongen krijgen geen willekeurige pijl. |
| 24 | Kaartknop voor een exacte omleiding als een bron echte routegeometrie levert. De huidige GTFS-alerts leveren die meestal niet; dan wordt geen omleidingspad verzonnen. |
| 25 | Vandaag toont vertrekborden van de eerste drie favoriete haltes; ververst tijdens gebruik. |
| 26 | Favorieten beheren: naam, groep en volgorde. |
| 27 | Lokale JSON-export en import met controle en bevestiging vóór samenvoegen. Geen sleutels of ritgeschiedenis in export. |
| 28 | Alleen favoriete lijnen op het haltebord; haltefilters blijven per halte bewaard. |
| 29 | Expliciete tabbladen Aankomsten en Vertrekken. Wisselen wist het oude bord tijdens het ophalen. |
| 30 | Haltes sorteren vanaf eigen live locatie of kaartmiddelpunt, met zichtbare keuze. Zonder locatie wordt kaartmiddelpunt gebruikt. |
| 31 | Toegankelijkheid volgens ontvangen gegevens; ontbrekende of niet bevestigde gegevens blijven als zodanig vermeld. Geen belofte over de hele reis. |
| 32 | Looproute naar een geselecteerde halte/perron via planner. Werkelijke GPS en toegangspaden blijven bepalend. |
| 33 | Relevante meldingen bij de actieve reis op basis van gekoppelde lijn-, halte- en vervoerder-ID's. |
| 34 | Zoek- en vervoerder/district/belangrijk/favorietenfilters op verstoringen; filterkeuzes lokaal bewaard. |
| 35 | Dubbele meldingen worden samengevoegd met behoud van betrokken lijnen, haltes en geldigheidsperioden. |
| 36 | Actueel, aangekondigd en afgelopen onderscheiden. Afgelopen meldingen verdwijnen uit het gewone overzicht. |
| 37 | Filter op belangrijke reiswijzigingen. |
| 38 | Optionele nieuwe/gewijzigde verstoringsmeldingen via Android, instelbaar op vervoerder, lijn, district, plaats/halte en favoriete lijnen. |
| 39 | Optionele officiële NS-koppeling voor samenstelling en treinstelnummers. Toegang en dekking zijn afhankelijk van jouw abonnement en de bron; nog lokaal te testen. |
| 40 | Merk/type alleen bij bevestigde register- of API-koppeling. Eigen SVG's variëren naar mode, typefamilie, lengte en delen. Generieke illustratie bij ontbrekende gegevens. |

## Overige veranderingen en oplossingen

- Eigen voertuigtekeningen: korte/lange/gelede bus, tram/metro en trein met aantal delen en dubbeldeksweergave voor bekende typefamilies. Dit zijn gestileerde illustraties, geen exacte technische schaaltekeningen of garantie dat ieder model een uniek carrosserieprofiel heeft. Een registeromschrijving kan minder specifiek zijn dan een commercieel modeltype. Onbruikbare waarden zoals N/A gelden niet als type.
- NeTEx-lengte toegevoegd bij 3.821 van 3.847 geregistreerde bussen. Onbekende lengte blijft onbekend.
- Qbuzz-district waar beschikbaar uit het voertuigregister. Lijnregio's bij meldingen zijn een beperkte indeling op bestemmingsnamen, niet een volledige officiële concessiedatabase; grensoverschrijdende lijnen kunnen daardoor anders ingedeeld zijn. Onbekend blijft apart filterbaar. Qbuzz Utrecht wordt niet als actuele vervoerder verzonnen.
- Omleidingen behouden oorzaakcodes, toelichting, alle geldigheidsperioden en de correcte halte-ID's. Expliciet benoemde vervallen haltes worden duidelijk gemarkeerd; andere geselecteerde haltes heten betrokken haltes. Zonder afzonderlijke halteverwijzingen blijft de beschrijving zichtbaar. Een onbekende oorzaak wordt niet als bekende oorzaak verzonnen.
- Geselecteerde halte wordt voor planning naar een bruikbaar halte-ID vertaald. Een stoparea wordt opgelost naar een bijbehorend perron; hierdoor verdwijnt de kunstmatige toegangswandeling bij dezelfde halte. Werkelijke loopstukken of perronwissels blijven zichtbaar.
- Geen overbodige vergelijkingstabel wanneer alle adviezen zonder overstap zijn. Marketingzinnen verwijderd.
- Automatische eigen locatie tijdens gebruik na Android-toestemming. Geen persoonlijke GPS op de achtergrond. Kaartlocatie en afstandssortering volgen nieuwe metingen zonder telkens op de locatieknop te drukken.
- Ritgeschiedenis bewaart de laatste twaalf lokaal bekeken/bewaakte ritten met daadwerkelijk ontvangen waarnemingen. Je kunt geen willekeurige oude rit terughalen die nooit ontvangen is. Ontbrekende tijden en halteafwijkingen blijven onbekend. GPS-passages worden niet omgezet in exacte historische aankomsttijden.

## Achtergrond en dataverbruik

Start een reis en schakel **Achtergrondbewaking** in, of zet een uitstapmelding aan met **Ook op achtergrond / bij scherm uit**. Android vraagt meldingstoestemming en toont een blijvende melding met stopknop. Bewaking gebruikt publieke voertuigposities en ritberichten; geen persoonlijke achtergrondlocatie. Zonder gekoppelde GPS kan de APK wachten op verse geregistreerde haltepassages, als de bron die levert. Voorspellingen activeren de uitstapmelding niet.

Reisinfo controleert onder andere perronwijziging, vervallen rit/halte en een krappe overstap. Dit is hulp, geen gegarandeerd alarm. Ontbrekende/stale GPS, onbekende meettijd, onduidelijke route, netwerkuitval en ontbrekende geregistreerde passages kunnen een uitstapmelding verhinderen. Na geforceerd stoppen van de app werkt de bewaking niet door. Android kan de foreground-datasyncdienst beëindigen; bij Android 15+ geldt een totale achtergrondlimiet van zes uur per 24 uur voor dit servicetype. Open de app om opnieuw te starten.

Verstoringsmeldingen staan standaard uit. Kies de filters via **Omleidingen → Meldingen instellen**. Eerste controle maakt een nulmeting. Daarna meldt Android nieuwe of gewijzigde passende berichten; bij gewijzigde instellingen begint een nieuwe nulmeting. Controle is ongeveer elke 15, 30 of 60 minuten en kan door Android worden uitgesteld. Het is geen onmiddellijke server-pushdienst. Instellingen worden op het toestel bewaard.

Geen eigen server of betaalde hosting. Rechtstreekse NDOV-berichten worden direct verwerkt; de app deelt één publieke liveverbinding tussen scherm en achtergrondbewaking. Landelijk overzicht wordt bij gewone kaartweergave circa elke 45 seconden gecontroleerd, bij een rit circa elke 20 seconden; bij verse directe metingen kan de grote download minder vaak. De nationale bron zelf ververst vaak ongeveer per minuut. Een maximale vertraging van 30 seconden kan dus niet worden gegarandeerd. Treinposities worden optioneel ongeveer elke 30 seconden opgevraagd. Sneller ophalen maakt een oude bron niet actueler.

Kaarttegels worden lokaal hergebruikt; favorietenborden vragen alleen tijdens gebruik periodiek data. Nationale feeds en een volledige vervoerdersstream blijven extra data kosten. Filtering op één bus vermindert de gedownloade vervoerdersstream niet. Controleer het Android-verbruik in Instellingen. De eigen tekeningen zijn lokale SVG's en kosten geen extra mobiele afbeeldingsdownloads. Gratis datadiensten kunnen limieten of beleid wijzigen.

## Controle en jouw test

Uitgevoerd: JavaScript-modeltests voor route/advies/voortgang/alerts/import; JVM-controles voor decoders, echte opgeslagen feeds en native bewakingslogica; APK-compilatie, uitlijning en handtekening. Browsercontrole van grote kaart, ritpagina, eerdere haltes, geschiedenis, favoriete vertrekborden, aankomsten, planner, filters, notificatie-instellingen en NS-invoerscherm. MagicPath gebruikt voor een eigen voertuig-/ritontwerp; Mobbin was niet beschikbaar onder het gekoppelde abonnement.

Niet op een fysiek Android-toestel of emulator geverifieerd: Android-toestemmingen, Keystore, lokale bestandskiezer, scherm-uitgedrag, echte meldingen en fabrikantgebonden energiebesparing. NS-antwoorden met jouw sleutel zijn eveneens nog niet getest. De browserpreview kan de Android-achtergronddienst niet uitvoeren.

Test op jouw OnePlus vooral:

1. Normale update-installatie en behoud van bestaande favorieten.
2. Eén bekende busrit: exacte halte, tussen twee haltes, eindpunt, vorige haltes en grote routekaart. Let op ✓ tegenover ↗ en onbekende gegevens.
3. Instellingen → NS API & treinen: sleutel lokaal invoeren; daarna kaartfilter/positie en Treinstellen bij een bekende treinrit controleren. Stuur alleen fouttekst, nooit je sleutel.
4. Begin een korte actieve reis, geef meldingstoestemming, zet achtergrondbewaking aan, wissel van app en zet scherm uit. Controleer de blijvende melding, uitstapmelding en stoppen via de melding. Controleer Android-batterijinstellingen als OnePlus de dienst beëindigt.
5. Verstoringsabonnement met smalle selectie; eerste baseline afwachten. Controleer latere echte wijzigingen en de inexacte controlefrequentie.
6. Favoriet hernoemen, verplaatsen, groep instellen, exporteren en weer importeren. Controleer aankomsten/vertrekken en bewaarde filters.
7. Eigen locatie laten bijwerken tijdens een rit zonder herhaald tikken; na achtergrond terugkeren en opnieuw een locatie zien. Weiger locatie om de zoekbare fallback te testen.

## Bronnen en technische grenzen

- [NS API-portaal](https://apiportal.ns.nl/) — persoonlijk API-product en toegang.
- [GTFS-Realtime-schema](https://gtfs.org/documentation/realtime/proto/) — onder meer EntitySelector.stop_id, oorzaken en perioden.
- [Transitous](https://transitous.org/api/) — planner en gebruiksvoorwaarden voor openbare apps.
- [Android foreground-service-limieten](https://developer.android.com/develop/background-work/services/fgs/timeout) — tijdslimiet dataSync.
- [Android JobScheduler](https://developer.android.com/reference/android/app/job/JobInfo.Builder#setPeriodic(long)) — minimumperiode en inexacte uitvoering.
- [Qbuzz-regio's](https://www.qbuzz.nl/reizen-met-qbuzz) — huidige regio-indeling.
- [NS over treinleveranciers](https://nieuws.ns.nl/ns-en-alstom-sluiten-contract-over-levering-van-79-snelle-intercitys/) — kleine typefamilie-mapping; geen bewijs van de samenstelling van een specifieke rit.

De broncode staat openbaar onder MIT. API-dekking blijft een eigenschap van de externe bron; ontbrekende informatie wordt zichtbaar gehouden.
