# Wijzigingen

## 0.4.1 — 2026-11-13

- Directe NS-ritkoppeling vanuit de kaart met Reisinformatie API, stationvolgorde en perrons.
- Materieeltype, treinstelnummer en aantal bakken uit officiële Virtual Train-velden; ook direct in het voertuigscherm.
- SpoorKaart-geometrie met veilige schematische fallback, controle op ritnummer/rijdag en behoud van geldige koppelingen bij nieuwe posities.
- Blijvende NS-status, verbindingstest en verversen van NS-posities via de kaartknop.
- 74 JS-tests, 11 NS-ritadaptertests en 12 native bewakings-/positietests. Mobiele UI-controle met gemarkeerde testgegevens.
- Zie docs/testgids-0.4.1.md voor bronbeperkingen en de nog noodzakelijke telefoontest.


## 0.4.0 — 2026-11-13

- Ideeën 21–40, met bronafhankelijke grenzen voor omleidingsgeometrie en NS-materieel.
- Grote aparte kaartpagina, originele SVG-voertuigen, kleinschalig gecentreerd routevoorbeeld en blijvende kaartknop op de ritpagina.
- Bevestigde haltebezoeken tegenover GPS-passages en onbekende bezoeken; eerdere haltes uitklapbaar en lokale ritgeschiedenis.
- Favorietenborden, beheer/export/import, aankomsten, bewaarde filters, eigen locatie tijdens gebruik.
- Alert-stop-ID-parser gecorrigeerd; oorzaken, perioden, scopes en configureerbare periodieke meldingen.
- Opt-in foreground-datasyncdienst voor reisinfo en uitstapmelding zonder eigen achtergrond-GPS; één gedeelde NDOV-verbinding.
- Optionele NS API-sleutel via lokale Keystore; echte API-dekking en Android-achtergrondgedrag nog te testen.
- Planner vertaalt stopareas naar halte-ID’s; kunstmatige toegangswandeling en onnodige directe-reisvergelijking opgelost.
- 63 JS-tests, 12 decodercontroles, 10 native modelcontroles; browsercontrole op mobiel formaat.


## 0.3.0 — 2026-11-12

- Eigen halte-/ritpagina en schermvullende kaart, ook zonder voertuig-GPS.
- Ideeën 1–20: vaste reizen, geschiedenis, planneropties, vergelijken, via, eerste/laatste, voorgrondherinneringen, actieve reis, overstapmarge, perronwijzigingen, delen, scherm-aan en kaart-/lijnfavorieten.
- Strengere ritkoppeling, antwoordvolgorde, realtimebetrouwbaarheid, kaartfilters en UI-correcties.
- 36 JS-tests, 11 decodercontroles en echte plannerproeven. Native gedrag op de OnePlus nog niet geverifieerd.
- Geen achtergrondbewaking; zie docs/testgids-0.3.0.md voor exacte grenzen.


## 0.2.0 — 12 november 2026

- Route en tussenhaltes worden automatisch geladen bij het volgen van een voertuig.
- Genummerde, aanklikbare routehaltes; vorige/huidige/volgende halte met duidelijke labels.
- Camera toont de bus met aangrenzende haltes. Verslepen pauzeert centreren; aparte knoppen voor centreren, routeoverzicht en stoppen.
- Afgelegd en resterend routegedeelte visueel gescheiden wanneer de positie betrouwbaar is.
- Volgkaart met haltecontext, punctualiteit en leeftijd van de positie.
- Halteoverzicht met perrons, beschikbare tijden, voortgang, snel naar de bus/begin en een vaste sluitknop.
- Expliciete onzekerheid bij oude posities, ontbrekende geometrie of afwijkingen van de route; schematische verbindingen zijn herkenbaar.
- Routecache en lokale voortgangsberekening; terugval op de gewone feed wanneer de gevolgde positie veroudert.
- Late antwoorden van een vorige volgactie kunnen een inmiddels gesloten/andere route niet opnieuw activeren.
- GTFS-haltevolgorde en voertuigstatus blijven beschikbaar in de native decoder.
- Tests voor 17 route- en GPS-situaties; bestaande decoderchecks behouden.
- Dezelfde APK-ondertekening als 0.1.0, versionCode verhoogd naar 2.

## 0.1.0 — 12 november 2026

Eerste persoonlijke testversie: reisplanner, voertuig-/haltekaart, vertrekborden met filters, voertuigregister, lokale favorieten en doorzoekbare verstoringen.
