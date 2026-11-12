# SlimOV 0.3.0 — testversie, ronde 1

Dit is de eerste uitbreidingsronde met de genummerde ideeën **1 t/m 20** uit de verbeterlijst. Ideeën 21–40 wachten op jouw feedback; deze versie heet nog geen V1.

Installeer `SlimOV-0.3.0.apk` over de vorige SlimOV heen. De pakketnaam en ondertekening zijn behouden, zodat Android je lokale gegevens bij een normale update kan bewaren. Verwijder de oude app daarvoor niet. De APK is circa 1,75 MB; mobiele downloads van kaarten en OV-data komen daar tijdens gebruik bij.

## Nieuw ritoverzicht en kaart

Open een halte, kies een vertrek en je krijgt een eigen ritpagina met alle haltes op volgorde. Dit werkt ook zonder voertuig-GPS. Bij verschillende aankomst- en vertrektijden staan beide onder elkaar. De ingesprongen tweede tijd is vertrek. Perrons en bekende wijzigingen staan bij de halte.

Tik op het kleine routekaartje voor de schermvullende kaart. De kaart hergebruikt dezelfde Leaflet-kaart en tegelcache. **Terug naar haltes** brengt je terug; Android-terug sluit eerst de schermvullende stand. **Volg voertuig** is alleen zichtbaar als de voertuigfeed dezelfde rit, datum en starttijd bevestigt. Een ritnummer alleen is onvoldoende.

Bij ontbrekende routegeometrie blijft een schematische route als zodanig aangeduid. De kleine paarse routevoorvertoning is een routevorm zonder achtergrondkaart, zodat daarvoor geen extra kaarttegels nodig zijn.

## De eerste 20 ideeën

| Nr. | Waar / wat testen | Werking en grens |
|---|---|---|
| 1 | Reisplanner → Bewaar vaste reis | Geef een eigen naam. Favorieten opent de reis met opgeslagen voorkeuren en een nieuwe aanvraag. Alleen lokaal. |
| 2 | Reisplanner → Recente locaties | Kies Van of Naar. Maximaal tien locaties; Wis recente locaties verwijdert de geschiedenis. |
| 3 | Reisvoorkeuren → Voorkeur / Max. overstappen | Maximum gaat naar de planner. De voorkeur rangschikt de ontvangen alternatieven; het is geen uitputtende zoektocht naar iedere mogelijke reis. |
| 4 | Toegestane vervoermiddelen | Uitsluitingen gaan naar de planner. Treinsoorten worden afzonderlijk doorgegeven zodat metro uitsluiten ook echt kan. Minstens één soort nodig. |
| 5 | Extra overstaptijd | 0–30 minuten extra gaat naar Transitous. In de bewaking wordt de marge niet nog eens opgeteld bij een loopduur waarin de planner haar al verwerkt heeft. |
| 6 | Vergelijk je reizen | Tabel met vertrek, aankomst, duur, lopen en overstappen; horizontaal schuiven op kleine schermen. Ster is de voorkeur binnen de ontvangen adviezen. |
| 7 | Verbinding → Eerste / Laatste | Eén aanvraag vanaf middernacht of aankomst vóór het einde van de gekozen kalenderdag. Geen onbeperkte zoeklus. Ontbrekende bronritten kunnen ontbreken in het resultaat. |
| 8 | Via | Kies een halte of station. Eén doorberekend reisadvies via die halte, geen twee losse adviezen. Via betekent niet automatisch uitstappen of verblijven. Adressen zijn hier niet ondersteund. |
| 9 | Rit → Uitstapherinnering | Kies 1–3 haltes vooraf. Vereist een exact gekoppeld voertuig. Oude, ontbrekende of afwijkende GPS levert geen uitstapmelding op. **In deze testversie uitsluitend op de voorgrond; scherm uit of andere app pauzeert de bewaking.** Android vraagt pas na inschakelen om meldingsrechten. Een melding kan door Android stil gezet worden. |
| 10 | Actieve reis → overstapkaarten | Verwachte aankomst/vertrek met loopmarge. Krap / waarschijnlijk haalbaar / onvoldoende gegevens. Beide specifieke gebeurtenissen moeten bruikbare realtime-data hebben; onbekende loopmarge of oude gegevens geeft geen geruststellend advies. Geen aansluitgarantie. |
| 11 | Start deze reis | Volgende stap bovenaan, met lijn, bestemming en perron. Volgt bron­tijden en de knop Deze stap afgerond; geen automatische persoonlijke instapdetectie. |
| 12 | Paarse actieve reiskaart bovenaan | Wissel naar Haltes of Favorieten en keer terug. De actieve reis blijft lokaal bewaard na opnieuw openen. Realtime moet daarna opnieuw worden bevestigd. |
| 13 | Actieve reis → Zoek alternatief | Uitval uit een ritupdate wordt prominent benoemd. Alternatieven worden alleen op jouw verzoek gezocht, vanaf de volgende reisstap. Controleer of dat vertrekpunt nog klopt. |
| 14 | Perrons | Bij bekende wijziging: tekst ‘Perron gewijzigd’, oud perron doorgestreept en nieuw perron erbij. Alleen een nieuw perron zonder bekend oud perron wordt niet als bevestigde wijziging voorgesteld. |
| 15 | Deel reis / Deel reisstatus | Androids deelmenu met leesbare tijden en traject. Jij kiest de bestemming en verstuurt; geen automatische berichten of live locatie delen. |
| 16 | Instellingen → Scherm aan tijdens volgen | Vrijwillig, lokaal onthouden. Alleen rit-, actieve reis- en routevolgschermen; de native vlag wordt bij achtergrond direct gewist. Batterijgebruik op je OnePlus nog testen. |
| 17 | Kaartfilters → Haltebenamingen | Relevante haltes, alle namen bij voldoende zoom, of alleen nummers/punten. Gewone haltes blijven op de kaart bij voldoende zoom. |
| 18 | Kaartfilters → Vervoerder / merk | Filtert de naam uit de dienstregeling. Een concessiemerk is niet altijd de uitvoerende vervoerder; onbekende uitvoerder wordt niet verzonnen. |
| 19 | Kaartfilters → Specifieke lijn en richting | Lijn-ID, vervoerder en richting samen. De lijst gebruikt de beschikbare voertuigfeed; een momenteel ontbrekende lijn staat er niet altijd in. Richting 0/1 is de richting uit de bron. |
| 20 | Kaartfilters of rit → Bewaar lijn | Bewaart exacte lijn/richting plus kaartgebied. Favorieten opent die uitsnede en filtercombinatie met één tik. Onbekende richting kan niet als bevestigde richting worden opgeslagen. |

## Opgeloste fouten en UI-problemen

- Oude ritantwoorden mogen een later gekozen rit niet overschrijven.
- Voertuigkoppeling controleert datum, starttijd en ritidentiteit. De oude terugval ‘zelfde voertuignummer’ mag geen positie van een andere rit overnemen.
- Een gewone ritkaart toont gekoppelde ritvoertuigen, in plaats van iedere bus met hetzelfde lijnnummer in beide richtingen.
- Volgen van een nieuw voertuig wist conflicterende kaartfilters.
- Android-terug behandelt ritpagina en schermvullende kaart apart.
- Ritpagina toont zowel aankomst als vertrek, en heeft een eigen terugknop.
- Kaart wisselt van formaat zonder een tweede kaartinstantie; overzicht gebruikt geen overlappende zoomanimatie. Een conflicterende hoogteregel bij een actieve reis is opgelost: ook dan vult de grote kaart de hele apphoogte.
- Haltenamen en tijden krijgen meer ruimte. Donkere knoppen hebben een lichter paars; Intercity krijgt een compacte IC-badge.
- De sluitknop van lange bladen blijft bereikbaar bij scrollen; filters passen op smalle schermen.
- Verouderde ritcache wordt niet als zojuist opgehaalde informatie gepresenteerd. Realtimekleur bij haltes hangt af van het betreffende tijdstip uit de bron.
- Geen technische START/END-namen in het actieve reisoverzicht.
- Mislukte lokale opslag geeft een melding in plaats van ongemerkt gegevens kwijt te raken.
- Geen dubbele overstapmarge, geen veilige aansluiting uit oude realtime en geen aangenomen nul minuten lopen bij ontbrekende loopinformatie.

## Uitgevoerde verificatie

- 36 JavaScript-tests voor route/GPS, ritidentiteit, overstappen, filters, voorkeuren, recente locaties, perrons en herinneringen.
- 11 Java-decodercontroles met echte voertuig- en meldingenbestanden, negatieve vertraging, KV6 en ongeldige invoer.
- Android-APK gecompileerd voor target 36 / minimum 31; APK-signatuur gecontroleerd. De ondertekenaar is gelijk aan de vorige update.
- Browsercontrole op mobiele afmetingen: ritlijst, echte treinroute zonder GPS, grote kaart en terug, planner met max. 1 overstap / 5 min extra / metro uitgesloten, alternatieven, actieve reis, navigatie, herladen met bewaarde reis, recente locaties, favoriete reis, vervoerder/lijn/richting en favoriete lijn.
- Drie begrensde API-proeven: via Hilversum, eerste en laatste verbinding. Alle drie leverden vier adviezen; uitgesloten metro en maximum overstappen bleven gerespecteerd.
- Licht en donker visueel gecontroleerd. Geen JavaScript-consolefouten in de gecontroleerde nieuwe schermen.

**Nog niet op een fysieke Android-telefoon uitgevoerd:** deelmenu, meldingsrechten en geluid, scherm-aan/uit, OnePlus-batterijbeheer, echt uitstappen, daadwerkelijke uitval/perronwissel, netwerkwissels en langdurig reizen. De bewakingslogica is getest, maar deze praktijksituaties zijn daarmee niet bewezen. Native achtergrondbewaking is niet gebouwd in deze ronde.

## Jouw testvolgorde

1. Update de APK. Controleer dat bestaande favoriete haltes er nog staan.
2. Open een busrit en een treinrit. Controleer haltevolgorde, tijden en perrons.
3. Tik op de kleine kaart; zoom, schuif, keer terug via knop én Android-terug.
4. Volg een echte bus. Controleer bij welke/tussen welke haltes hij staat. Test relevante namen en alleen nummers.
5. Bewaar een reis en een lijn. Sluit/heropen SlimOV en open beide uit Favorieten.
6. Plan met één overstap, metro uit, 5 min extra, via, en eerste/laatste van een dag. Vergelijk de adviezen.
7. Start een reis, open een halte en ga terug via de paarse reiskaart. Probeer stap afgerond en deelmenu.
8. Zet een uitstapherinnering vóór een toekomstige halte aan. Houd SlimOV vooraan open. Controleer de melding; controleer ook dat zonder GPS geen nepwaarschuwing wordt gegeven.
9. Zet Scherm aan tijdens volgen aan. Navigeer naar gewone Favorieten en wissel naar een andere app: de scherm-aan-vlag hoort vrijgegeven te zijn.
10. Noteer per probleem: scherm, handelingen, verwachte/werkelijke uitkomst, lijn/halte en tijd. Deel geen privébestemming als die niet nodig is.

## Ontwerp en gratis bronnen

De ritpagina is uitgewerkt met de MagicPath-plugin en vertaald naar SlimOVs bestaande lichte/donkere paarse stijl. De Mobbin-plugin is geprobeerd, maar weigerde toegang wegens een betaald abonnement; er is niets aangeschaft en er zijn geen onbezien gevonden referenties gebruikt. Jouw OVinfo-screenshots waren de inhoudelijke referentie.

SlimOV gebruikt de bestaande openbare [broncode](https://github.com/lewis091dc/slimOV), [Transitous](https://transitous.org/api/) en openbare OV-data. Er is geen eigen server of betaald abonnement toegevoegd. De gratis communitybron werkt op best-effort-basis en biedt geen gegarandeerde beschikbaarheid of maximale vertraging. Kaarttegels en de directe vervoerdersfeed kunnen mobiele data gebruiken; een lijnfilter reduceert alleen wat wordt getoond, niet de ontvangen vervoerdersfeed.

Android-meldingen gebruiken de [standaard meldingsrechten](https://developer.android.com/develop/ui/views/notifications/notification-permission). We vragen geen exact-alarm-, achtergrondlocatie- of opstartrechten voor deze voorgrondtestversie.

