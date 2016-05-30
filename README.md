# parkeermij
Tamme Thijs
tammethijs@gmail.com
june 2016

#Probleem
Parkeren in Amsterdam.

#oplossing
Door middel van een android applicatie mensen die in Amsterdam willen parkeren helpen om een plek te vinden.
####summary
 - location based; gebruiker kan dmv GPS zijn huidige locatie gebruiken als startpunt, of een locatie kiezen vanuit google maps.
 - API: http://divvapi.parkshark.nl/.
 - Call naar api met positie + tijd, hierop krijg ik een lijst met mogelijke plekken terug.
 - Deze sorteren op tijd en/of kosten en laten zien aan de gebruiker.
 - Gebruiker kan een route selecteren waarna ik deze laat zien op googlemaps.

#data
- Zoals eerder vermeld gebruik ik http://divvapi.parkshark.nl/, iets wat ondersteund is door de gemeente
Amsterdam. (http://data.amsterdam.nl/dataset/parkeer_api/resource/82e9b535-10f8-4d6b-bac0-c2b1aea398b9)
- Deze data komt binnen als JSON, deze omzetten in objecten.

#onderdelen/opzet
###landing page
Hier kan de gebruiker selecteren om huidige locatie te gebruiken of zelf een locatie in te voeren. Beide opties
resulteren in feedback door een locatie op googlemaps te laten zien.

###maps
Laten zien welke locatie is ingevuld, en (optioneel) deze via google maps aan te passen.


 




