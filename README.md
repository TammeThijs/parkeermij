# parkeermij
######tammethijs@gmail.com
######june 2016

###Probleem
Parkeren in Amsterdam.

###oplossing
Door middel van een android applicatie mensen die in Amsterdam willen parkeren helpen om een plek te vinden.

 - Location based; gebruiker kan dmv GPS zijn huidige locatie gebruiken als startpunt, of een locatie kiezen vanuit google maps.
 - Call naar api met positie + tijd, JSON met routes terug.
 - Deze sorteren op tijd en/of kosten en laten zien aan de gebruiker.
 - Gebruiker kan een route selecteren waarna deze getoond wordt in  googlemaps.

###data
- API routes: http://divvapi.parkshark.nl/, ontwikkeld in samenwerking met gemeente amsterdam
Amsterdam. (http://data.amsterdam.nl/dataset/parkeer_api/resource/82e9b535-10f8-4d6b-bac0-c2b1aea398b9)
- API google maps.
- API locatie.
- (optioneel) API auto complete adressen

###onderdelen/opzet
#####landing page
Hier kan de gebruiker selecteren om huidige locatie te gebruiken of zelf een locatie in te voeren. Beide opties
resulteren in feedback door een locatie op googlemaps te laten zien.

#####maps
Locatie laten zien op google maps. (waarschijnlijk weg)

#####routes
routes laten zien met ~5-10 resultaten, deze kunnen gefilterd worden op kosten of afstand.
- Als route wordt geselecteerd wordt deze getoond in maps.


#####optioneel
- lijst met gebruikte locaties opslaan
- realtime bezettingsgraad parkeergarage
- locaties parkeermeters bij locatie


###vergelijkbare apps
http://www.parkshark.nl/
Gebruikt dezelfde API, echter wel alleen iOS en verouderd.



 




