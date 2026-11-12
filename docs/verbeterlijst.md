# SlimOV: uitgebreide verbeterlijst

Deze lijst bevat **60 concrete vervolgideeën**. Het zijn voorstellen, geen functies die allemaal al gebouwd zijn. De bestaande afspraak blijft: persoonlijk gebruik, geen eigen continu draaiende computer, geen betaald abonnement en zuinig met mobiele data.

## Nu toegevoegd in versie 0.2.0

- Bij **Volg live** worden de ritroute en tussenhaltes automatisch opgehaald.
- Haltes hebben een volgnummer. De halte bij de bus en de volgende halte worden extra benadrukt; haltebenamingen staan bij de relevante punten.
- De kaart houdt de bus en aangrenzende haltes samen in beeld. Verslepen onderbreekt automatisch centreren; **Centreer bus** schakelt het weer in.
- Het al afgelegde traject wordt grijs en het resterende traject paars, wanneer de positie betrouwbaar op de bekende route te plaatsen is.
- Een kaartje toont **bij welke halte** of **tussen welke twee haltes** de bus rijdt, met de leeftijd van de positie.
- **Alle haltes** opent een geordende lijst met perrons, beschikbare tijden en voortgang. De lijst opent bij de bus; **Vanaf begin** toont de hele route vanaf het begin.
- Bij oude GPS-data, een positie buiten de bekende route of ontbrekende routegeometrie meldt de app dat de volgende halte onzeker is. Een schematische verbinding wordt niet als exact wegverloop voorgesteld.
- Routegegevens worden kort hergebruikt. De voortgang wordt lokaal berekend; er is daarvoor geen extra GPS-polling of eigen server nodig. Als de directe voertuigmeldingen verouderen, kan de normale voertuigfeed weer vaker als terugval dienen.

**Belangrijk:** “voorbij” en “volgende halte” zijn een afleiding uit de laatste voertuigpositie en de opgehaalde route. Ze zijn geen bewijs dat het voertuig daadwerkelijk bij iedere halte is gestopt. Een exacte aankomsttijd wordt niet uit alleen de beweging verzonnen.

## Eerst aanpakken

| Prioriteit | Verbetering | Waarom nuttig | Afhankelijkheid |
|---|---|---|---|
| 1 | Uitstapherinnering | Je mist je halte minder snel. | Notificaties, actuele positie en zorgvuldig batterijbeheer. |
| 2 | Overstapbewaking | Waarschuwt als de aansluiting krap wordt. | Verwachte tijden van beide ritten; nooit als garantie presenteren. |
| 3 | Favoriete reizen | Een vaste reis plannen met één tik. | Lokaal op te slaan, zonder account. |
| 4 | Lijnen als favoriet | Je vaste buslijn snel op de kaart openen. | Lijn, vervoerder en richting samen bewaren. |
| 5 | Haltevertrekken op het startscherm | Direct zien wanneer je gebruikelijke bus komt. | Kleine, gerichte vertrekbordaanvragen. |
| 6 | Favorieten exporteren/importeren | Je gegevens bewaren bij opnieuw installeren. | Lokaal bestand; geen cloud verplicht. |
| 7 | Gerichte gegevensupdates | Haltes en voertuigregister bijwerken zonder nieuwe APK. | Een veilige, gratis distributiebron en versiecontrole. |
| 8 | Treinmaterieel koppelen | Treinstelnummer en model tonen als de bron ze levert. | Toegang, gebruiksvoorwaarden en dekking eerst onderzoeken. |

## Reizen plannen

1. **Favoriete reizen opslaan — hoog.** Bewaar bijvoorbeeld Thuis → School met een zelfgekozen naam, zonder een account.
2. **Recente zoekopdrachten — hoog.** Toon de laatste vertrekpunten en bestemmingen; bied ook een wisfunctie.
3. **Maximaal aantal overstappen — hoog.** Laat kiezen tussen weinig overstappen of de snelste reis, voor zover de planner dit ondersteunt.
4. **Vervoermiddelen uitsluiten — hoog.** Bijvoorbeeld alleen bus en trein, of metro vermijden. Controleer welke filters de bestaande planner ondersteunt.
5. **Extra overstaptijd — hoog.** Geef ruimte voor rustig lopen of een onbekend station; maak duidelijk of dit door de planner wordt meegenomen.
6. **Alternatieven vergelijken — midden.** Zet duur, lopen, overstappen en vertrektijd naast elkaar, met een duidelijke voorkeur.
7. **Eerste of laatste verbinding — midden.** Een gerichte zoekoptie voor vroeg vertrekken of laat thuiskomen, zonder onbeperkt automatisch veel reisadviezen op te vragen.
8. **Tussenbestemming — later.** Een extra stop in de reis, met onderscheid tussen één doorberekende reis en twee aparte adviezen.

## Onderweg en overstappen

9. **Uitstapherinnering — hoog.** Laat een melding geven vóór een gekozen halte, met een instelbare waarschuwing. Test onzekere of ontbrekende GPS expliciet.
10. **Overstapbewaking — hoog.** Vergelijk verwachte aankomst en vertrek met benodigde looptijd. Toon “krap”, “waarschijnlijk haalbaar” of “onvoldoende gegevens”.
11. **Volgende reisstap bovenaan — hoog.** Toon na een busrit direct het volgende perron, de volgende lijn en de bestemming.
12. **Doorlopende reiskaart — midden.** Bewaar één actieve reis, zodat je vanuit de haltepagina weer terug kunt naar het juiste traject.
13. **Uitval tijdens een actieve reis — hoog.** Benoem een vervallen rit nadrukkelijk en laat een alternatief op aanvraag zoeken.
14. **Perronwijziging benadrukken — hoog.** Zet gepland en actueel perron naast elkaar en gebruik ook tekst/icoon, niet alleen kleur.
15. **Reisstatus delen — midden.** Deel een leesbare reistijd of reislink via Androids deelmenu; geen locatie automatisch naar anderen sturen.
16. **Scherm aan tijdens volgen — midden.** Een vrijwillige instelling die alleen voor het actieve volgscherm geldt en weer uitgaat bij stoppen.

## Kaart en routes

17. **Instelbare haltebenamingen — hoog.** Keuze tussen alleen relevante haltes, alle namen bij voldoende zoom of alleen nummers.
18. **Filter op vervoerder — hoog.** Open bijvoorbeeld alleen EBS of Arriva, met herkenbaar verschil tussen vervoerder en merknaam.
19. **Filter op specifieke lijn en richting — hoog.** Bewaar de volledige combinatie, zodat lijn 5 in twee regio’s niet wordt verwisseld.
20. **Lijnen als favoriet — hoog.** Eén tik opent de gewenste lijn, richting en kaartuitsnede.
21. **“Wat rijdt hier?” — midden.** Een lijst met lijnen in het zichtbare kaartgebied, zonder elke lijn daarvoor apart te downloaden.
22. **Voertuigbeweging vloeiender tekenen — midden.** Interpolatie tussen metingen, met zichtbaar onderscheid tussen gemeten en geschatte positie. Nooit geschatte beweging als nieuwe GPS-data presenteren.
23. **Rijrichting op voertuigicoon — midden.** Een pijltje gebruiken wanneer richting beschikbaar of verantwoord afleidbaar is; bij stilstand geen willekeurige draaiing.
24. **Omleiding op de kaart — later, bronafhankelijk.** Alleen een exacte alternatieve route tekenen wanneer de bron de geometrie of voldoende route-informatie levert.

## Haltes, perrons en favorieten

25. **Vertrekbord van favoriete halte op het startscherm — hoog.** Direct de eerstvolgende vertrekken, met een begrensde verversing.
26. **Favorieten ordenen en hernoemen — hoog.** Sleepvolgorde of pijltjes, eigen namen en eventueel mappen zoals Werk en Privé.
27. **Favorieten exporteren/importeren — hoog.** Een klein JSON-bestand met duidelijke melding over welke gegevens worden gedeeld.
28. **Alleen favoriete lijnen bij een halte — hoog.** Bewaar filters per halte zodat je ze niet steeds opnieuw instelt.
29. **Aankomsten naast vertrekken — midden.** Een schakelaar met expliciete labels; een aankomst niet verwarren met een gegarandeerde vertrektijd.
30. **Haltes op afstand sorteren — hoog.** Laat kiezen tussen afstand tot eigen locatie of afstand tot het kaartmiddelpunt; benoem welke wordt gebruikt.
31. **Toegankelijkheid tonen — midden, bronafhankelijk.** Toon bekende informatie over instap en voorzieningen, met “onbekend” wanneer gegevens ontbreken.
32. **Wandelroute naar de instaphalte — midden.** Voorkom een lijn naar het verkeerde perron; controleer of de bestaande planner hiervoor bruikbare loopgeometrie levert.

## Verstoringen en omleidingen

33. **Meldingen bij de actieve reis — hoog.** Alleen relevante lijnen, stations en haltes tonen, naast de landelijke zoekpagina.
34. **Zoekfilters bewaren — midden.** Laat regio, vervoerder of favoriete lijnen snel opnieuw kiezen.
35. **Dubbele meldingen samenvoegen — hoog.** Combineer gelijkluidende bronberichten, maar behoud de betrokken lijnen en haltes.
36. **Geldigheidsperiode zichtbaar maken — hoog.** Toon begin/einde als de bron die levert en onderscheid lopende van aangekondigde maatregelen.
37. **Alleen belangrijke meldingen — midden.** Een filter voor uitval, vervallen halte, omleiding of perronwijziging.
38. **Melding bij een gewijzigde favoriete lijn — later.** Alleen met expliciete toestemming; onderzoek of dit zonder eigen backend voldoende betrouwbaar en zuinig kan.

## Voertuigen en treinen

39. **Treinstelnummer en samenstelling — hoog, bronafhankelijk.** Onderzoek een toegestane NS-koppeling en onderscheid treinnummer, treinstelnummer en afzonderlijke delen.
40. **Treinmodel en fabrikant — hoog, bronafhankelijk.** Koppel een werkelijk geïdentificeerd stel aan bevestigd materieel; niet raden op basis van alleen “Intercity”.
41. **Meer officiële busregisters — hoog.** Breid het bestaande register uit wanneer andere vervoerders een geschikte, herbruikbare bron leveren.
42. **Voertuigregister automatisch bijwerken — hoog.** Download alleen wijzigingen of kleine nieuwe bestanden, met bron- en versiedatum in beeld.
43. **Voertuigvoorzieningen — midden.** Lengte, aandrijving, rolstoelplaats of lage vloer alleen tonen wanneer de gegevens voldoende specifiek zijn.
44. **Eigen voertuiggegevens exporteren — midden.** Lokale aanvullingen kunnen bewaren en herstellen, met onderscheid tussen eigen invoer en officiële gegevens.
45. **Zoeken op voertuignummer — midden.** Altijd ook de vervoerder laten kiezen; hetzelfde nummer kan meerdere betekenissen hebben.
46. **Voertuigwissel herkennen — later, bronafhankelijk.** Waarschuw wanneer een andere bus aan dezelfde rit wordt gekoppeld. Maak de keuze “volg de rit” tegenover “volg dit voertuig” expliciet.

## Minder data en batterijgebruik

47. **Datateller voor deze sessie en per dag — hoog.** Duidelijker dan alleen de Android-teller sinds toestelstart; benoem meetbeperkingen.
48. **Instelbaar datadoel — hoog.** Geef bijvoorbeeld een waarschuwing of pauzeer de snelle feed na een zelfgekozen hoeveelheid data, zonder stilzwijgend de app onbruikbaar te maken.
49. **Livefeed alleen via wifi — midden.** Een optionele instelling; op mobiel blijven dan het zuinige overzicht en handmatige verversing beschikbaar.
50. **Verversing per scherm aanpassen — hoog.** Geen landelijke voertuigdownload nodig wanneer je alleen een favorietenlijst of tekstpagina bekijkt.
51. **Kleine gegevensupdates via wifi — hoog.** Haltes, routes en voertuigregisters bij voorkeur bijwerken via wifi, met een handmatige uitzondering.
52. **Cache beheren — midden.** Toon hoeveel kaart- en brongegevens lokaal staan en bied afzonderlijk wissen aan, zonder favorieten te verwijderen.
53. **Duidelijke offlineweergave — hoog.** Laat opgeslagen haltes en favorieten werken; zet bij eerder opgehaalde tijden en posities altijd de ouderdom.
54. **Batterijcontrole op het eigen toestel — hoog.** Meet juist bij langdurig volgen, appwissels, scherm uit en hervatten; optimaliseer op metingen in plaats van aannames.

## Bedienbaarheid, kwaliteit en distributie

55. **Grotere tekst en schermlezer — hoog.** Controleer Androids tekstgrootte, TalkBack, voldoende tikoppervlak en tekst naast alle statuskleuren.
56. **Vaste sluitknop bij lange lijsten — hoog.** Laat een halte-/rittenblad altijd eenvoudig sluiten, ook nadat je ver naar beneden bent gescrold.
57. **Crash- en foutinformatie zonder tracking — hoog.** Een lokaal foutlog dat je bewust kunt exporteren; geen automatische upload van locaties of reisgeschiedenis.
58. **Controle op nieuwe APK — midden.** Toon versienummer en wijzigingslijst; laat de gebruiker zelf downloaden en installeren. Bewaar dezelfde ondertekening voor updates.
59. **Bronstatuspagina — hoog.** Toon apart of kaarttegels, voertuigfeed, directe feed, reisplanner en meldingen bereikbaar zijn; voorkom één misleidend algemeen “live”-lampje.
60. **Praktijktest op de OnePlus — hoog.** Loop een echte reis na: bus op kaart, instappen, meerdere haltes, een overstap, GPS-uitval, mobiele-datawissel en Android-teruggebaren.

## Voorstel voor de volgende versies

**0.3:** favoriete reizen/lijnen, filters onthouden, favorieten exporteren en duidelijkere offline-/bronstatus. Dit zijn uitbreidingen die goed bij persoonlijk en gratis gebruik passen.

**Daarna:** uitstapherinnering en overstapbewaking, eerst met praktijktests op de telefoon. Ze zijn pas nuttig als meldingen tijdig werken zonder onnodig batterij- of datagebruik.

**Als apart dataonderzoek:** treinstellen, volledige treinenkaart en extra voertuigregisters. Beschikbaarheid, gebruiksvoorwaarden en eventuele sleutels bepalen wat gratis en betrouwbaar kan worden toegevoegd. Geen betaalde bron activeren zonder jouw opdracht.
