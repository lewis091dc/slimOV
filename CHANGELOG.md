# Wijzigingen

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
