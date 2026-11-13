# SlimOV 0.4.1 — NS-rit en materieel koppelen

Installeer deze APK over 0.4.0; verwijder de bestaande app niet. Pakketnaam en ondertekening zijn behouden. Dit is een reparatie binnen ronde 21–40, nog geen V1 of ronde 41–60.

## Gebruik

1. Bewaar je bestaande Ns-App-sleutel in **Instellingen → NS API & treinen**. Een al opgeslagen sleutel hoeft bij een normale update niet opnieuw ingevoerd te worden.
2. Kies daar **Verbinding testen**. De status benoemt het aantal ontvangen posities of de foutcode. **Toon ontvangen treinen** wist kaartfilters en past de kaart aan het ontvangen treinbeeld aan.
3. Tik op een trein op de kaart. SlimOV vraagt nu rechtstreeks de samenstelling op: type, treinstelnummers en aantal bakken waar de bron die levert.
4. Kies **Rit & haltes** om de volledige rit met stations, tijden en perrons op te halen. **Volg live op de kaart** koppelt eerst dezelfde rit en opent vervolgens de volgkaart.
5. Vanuit de ritpagina opent **Routekaart** de gekoppelde kaart. **Overzicht** toont het hele traject. Doorreizen en eerder bekeken stations blijven bereikbaar via de gewone ritlijst.

De algemene testgids voor ronde 21–40 staat in testgids-0.4.0.md.

## Wat is gerepareerd

- De officiële API-definities zijn via het ingelogde NS-portaal geëxporteerd en gecontroleerd voor Virtual Train, Reisinformatie en SpoorKaart. De sleutel is niet uitgelezen of overgenomen.
- Materieel wordt nu ook vanuit de trein op de kaart opgevraagd. Correcte bronvelden `materieelType` en de lijst `bakken` worden verwerkt; voorheen kon een generieker type of een ontbrekend aantal bakken verschijnen.
- NS-ritverloop is rechtstreeks gekoppeld via Reisinformatie `/api/v2/journey`. Een trein wordt niet langer alleen naar de algemene reisplanner verwezen.
- Stations worden op basis van de bronverwijzingen `previousStopId` en `nextStopId` geordend. Alleen gebeurtenissen van het gekozen ritnummer tellen mee. Een onduidelijke vertakking levert een zichtbare fout op.
- Doorgereden stations helpen bij spoorgeometrie, maar worden niet als instap-/uitstaphalte gepresenteerd.
- SpoorKaart-geometrie wordt waar beschikbaar opgevraagd en gecontroleerd op verbonden delen, oriëntatie en aansluiting bij begin- en eindstation. Zonder bruikbare geometrie blijft de route expliciet schematisch. Een gap groter dan de toegestane aansluitmarge wordt niet met een willekeurige rechte verbinding overbrugd.
- Ritkoppelingen gebruiken nummer en een vast opvraagmoment; tijdvenster en versheid worden gecontroleerd. Nieuwe positieoverzichten behouden een nog geldige koppeling en laten een verlopen koppeling los. Een antwoord op een oude aanvraag mag een gewiste sleutel niet opnieuw activeren.
- Rijtuigen en stelnummer verschijnen in het voertuigscherm en de ritpagina. Tegenstrijdige meldingen ‘type niet beschikbaar’ worden op het NS-voertuigscherm verwijderd zodra de samenstelling apart wordt getoond.
- De kaartverversknop haalt ook NS-posities op. NS-fouten blijven zichtbaar via de NS-statusknop en de verbindingstest.
- Compacte tijdzone-offsets zoals `+0100` worden ondersteund.

## API-toegang en grenzen

Hetzelfde lokaal bewaarde Ns-App-abonnement wordt gebruikt voor de drie NS-diensten op gateway.apiportal.ns.nl. Als het product niet alle diensten toestaat, kan de ene functie werken en een andere 401/403 geven. SlimOV toont dan de fout; je hoeft geen sleutel in een bericht te delen. De sleutel wordt alleen op je toestel ontsleuteld voor de vaste officiële NS-host en komt niet in broncode, exports of deze testgids.

Een opgeslagen sleutel is geen bevestiging van toegang. HTTP 401/403 betekent dat de bron de aanvraag niet accepteert of de toegang niet toestaat; HTTP 404 betekent dat het gebruikte API-adres niet beschikbaar is. Een succesvol maar leeg of onbekend antwoordformaat wordt afzonderlijk benoemd.

De treinpositiebron levert volgens de huidige definitie geen meettijd. SlimOV benoemt daarom de ophaaltijd. De positie kan op de kaart worden getoond en aan de rit worden gekoppeld, maar hiermee worden geen exacte historische haltebezoeken of betrouwbare GPS-uitstapmeldingen verzonnen. NS `actualTime` zonder waarnemingsbewijs wordt als actuele verwachting verwerkt, niet als bevestigde passage. Daardoor kunnen eerdere stations nog ‘bezoek niet bevestigd’ tonen.

Samenstelling kan per vervoerder/rit ontbreken. Materieelgegevens zijn momentopnamen van de bron, geen garantie dat de formatie ongewijzigd blijft. De fabrikantmapping gebruikt alleen bekende typefamilies; onbekend blijft onbekend. Kleine gestileerde SVG's zijn geen exacte technische tekeningen.

Er wordt alleen voor een geselecteerde trein extra rit-/materieelinformatie opgehaald. Caches beperken herhaalde aanvragen. Het algemene treinpositieoverzicht blijft circa iedere 30 seconden verversen tijdens gebruik. Dit is geen garantie op een meetvertraging onder 30 seconden.

## Verificatie

- 74 JavaScript-tests: waaronder materieelvelden, verbonden spoorgeometrie, verkeerde rijdagen, verlopen koppelingen en late antwoorden na sleutelwijzigingen.
- 11 JVM-tests voor de officiële Journey-schema-adapter, onder meer grafiekvolgorde, verkeerde rit, vertakking, doorgereden station en tijdzone-offset.
- 12 JVM-tests voor positie-/bewakingslogica, waaronder onbekende NS-antwoorden versus een geldige lege treinlijst.
- Decodercontroles van de bestaande publieke voertuig- en verstoringsfeeds.
- Mobiele browsercontrole met duidelijk gemarkeerde **TESTGEGEVENS**: trein openen → samenstelling → Rit & haltes → routekaart, zonder browserfouten.
- APK-compilatie en handtekeningcontrole. Geen privégegevens of sleutels in het bronpakket.

Echte NS-antwoorden met jouw sleutel zijn niet door mij opgevraagd. Controleer op je telefoon de verbindingstest, een bekende trein en zijn rit/materieel. Stuur bij een probleem het ritnummer, de vervoerder, het tijdstip en de fouttekst, zonder sleutel. Android-achtergrondgedrag blijft een toestelspecifieke test uit de eerdere testgids.

## Officiële documentatie

- [Virtual Train API](https://apiportal.ns.nl/api-details#api=virtual-train-API)
- [Reisinformatie API](https://apiportal.ns.nl/api-details#api=reisinformatie-api)
- [SpoorKaart API](https://apiportal.ns.nl/api-details#api=spoorkaart-api)
- [NS abonnementen en sleutels](https://apiportal.ns.nl/startersguide)

De geëxporteerde definities zijn alleen lokaal gebruikt als documentatie. Het openbare bronpakket bevat de SlimOV-adapters en eigen tests, geen accountgegevens of gekopieerde persoonlijke sleutels.
