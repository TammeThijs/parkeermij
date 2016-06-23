#Parkeermij App
<img src="https://github.com/TammeThijs/parkeermij/blob/master/doc/MapsFragment2.PNG" align="right" height="326" width="183" >
Het doel van de Parkeermij app is dat de gebruiker, op basis van GPS locatie, opties krijgt om te parkeren in Amsterdam. Zo kunnen alle
parkeergebieden inclusief locaties van parkeermeters getoond worden waarna de gebruiker kan kiezen om een route naar een van deze meters
te starten. Door het overzicht van de verschillende gebieden in Amsterdam, en een leganda die de prijs per gebied aangeeft, geeft dit de 
gebruiker een goed overzicht over de mogelijkheden qua parkeren en prijs.
#
Naast de parkeermeters wordt er ook een aantal parkeergarages 
getoond aan de gebruiker, deze zijn te zien in een lijst die te sorteren is op prijs of afstand en uiteraard kan ook hier een route naartoe
worden gestart.

###Design
#####Design Overview
<img src="https://github.com/TammeThijs/parkeermij/blob/master/doc/Parkeermij%20Diagram.png" align="middle">

#####Design patroon
In dit project heb ik er voor gekozen om het Model View Controller (MVP) design patroon aan te houden. Ik was hier al wel eens eerder in aanraking mee gekomen bij het bouwen van een app, echter was dit de eerste keer dat ik dit compleet heb geïntegreerd binnen een zelfgemaakte app. Het principe van MVP rust op het beter bekende MVC binnen web development, echter zijn er wat unieke omstandigheden bij app development waardoor MVP in het leven is geroepen. De verschillende lagen dienen elk voor een eigen functie, in dit geval:
- View (Activities/Fragments) regelen 'front-end' handelingen zoals animaties, popups en het starten van een activity.
- Presenter (redelijk gelijk aan de controller) regelt alle logica, zoals user input en OnClick events
- Interactor, dit is de enige laag waar data requests gedaan mogen worden, bijvoorbeeld bij het ophalen van JSON etc.

Voor het 'binden' van deze lagen heb ik er voor gekozen om de Dagger library te gebruiken, voor elke activity kan ik dan een module + component opstellen waarbij ik met oa annotaties aan kan geven wat de relaties zijn tussen de activitie en achterliggende lagen.

#####Netwerk
Voor het ophalen van data heb ik gebruik gemaakt van Retrofit, een 'type-safe REST client'. Hoewel ik zelf nog nooit met retrofit had gewerkt (wel eens gezien) weet ik wel dat vrijwel alle bedrijven etc hier gebruik van maken en daarom wou ik dit graag leren tijdens dit project. Zeker bij de eerste API kostte dit erg veel tijd om goed geïmplementeerd te krijgen, wanneer het noodzakelijk was om een tweede API te gebruiken ging dit al vele malen sneller.

#####Classes Overzicht

Activities
- StartUpActivity
- BaseActivity

Fragments
- MapsFragment
- RoutesListFragment

Adapters
- RouteAdapter

Components
- BaseActivityComponent
- StartUpActivityComponent

Modules
- BaseActivityModule
- StartUpActivityModule

Presenters
- BaseActivityPresenter
- StartUpActivityPresenter

Interactors
- BaseActivityInteractor
- StartUpActivityInteractor

Helpers
- CustomRenderer
- PolygonHelper

Models
- ClusterObject
- CoordinatesWrapper
- LocationObject
- MeterObject
- RouteList
- RouteObject

Verder meest van de bovenstaande classes een Interface

#####Toelichting

In de eerste activity, de StartUpActivity, wordt voornamelijk data verzonden en opgehaald. Wanneer de activity start wordt er via GPS de huidige locatie van de gebruiker opgevraagd. Vervolgens wordt deze naar de ParkShark API gestuurd en de teruggegeven data omgezet in objecten. Tijdens de start, en bij het contact maken met de API wordt zowel de internet verbinding gecheckt als GPS. Mocht de GPS uitstaan dan krijgt de gebruiker een optie om deze aan te zetten. In geval van geen internet krijgt de gebruiker hier een bericht van. Mocht dit succesvol zijn dat wordt automatisch de BaseActivity gestart, in deze activity is dus als alles goed gaat geen input van de user nodig.
#
In de base activity zelf wordt de meegegeven data vanuit de startUpActivity opgehaald, en tergelijkertijd een nieuwe request gestuurd naar de API van het RDW om alle parkeerpalen op te halen binnen een 10km radius van centrum amsterdam. Dit gebeurd uiteraard allemaal asynchroon. Ook worden de menu items in de toolbar, en de navigationdrawer geïnitialiseerd. Aan de hand van welk van de twee fragments momenteel zichbaar is veranderen de functies van het toolbar menu; bij de mapsfragment is er de opties om meters en/of garages te tonen, in de routefragment kunnen deze gesorteerd worden op afstand of prijs. Deze settings worden opgeslagen door middel van sharedpreferences. Wanneer de data is opgehaald (succesvol of niet) wordt de MapsFragment geinflate.
#
De MapsFragment betreft de grootste functionaliteit voor de user van deze app. Nadat alle data is opgehaald uit de intent wordt hierin wordt de map geladen, parkeergebieden van amsterdam getekend en markers van garages en/of meters getoond. Een route naar een geselecteerde marker kan worden getoond door een icon binnen google maps zelf. Een erg grote uitdaging was dit zo soepel mogelijk te laten verlopen, al merkte ik snel dat dit erg veel vroeg van je mobiel. Meer hier over bij het kopje challenges.
#
De RouteListFragment laat de lijst van gevonden garages zien d.m.v een recyclerview. In de adapter kan deze m.b.v een comparator gesorteerd worden op prijs of afstand. De voorbeelden van locaties worden getoond door gebruik te maken van statische google images, die kunnen worden opgevraagd door het combineren van latitude en longitue tot een URL. Het tonen van deze images wordt gedaan door de library Picasso die hebt bij het asynchroon laden van images iets wat vrijwel noodzakelijk is bij gebruik van een list/recycler view.
#
Qua models spreekt het redelijk voor zich, naast de MeterObject en RouteObject was het noodzakelijk om een LocationObject te gebruiken voor het opslaan van de huidige locatie van de gebruiker. Dit omdat het model wat terug wordt gegeven door de GoogleClient niet serializable is en dus niet zonder problemen kan worden doorgegeven tussen activities. ClusterObject werd noodzakelijk door het gebruik van de Google Maps Utils waarbij custom markers alleen kunnen worden geplaatst door classes die ClustItem implementeren. 

###Uitdagingen

De eerste grote uitdagingen was het gebruik van retrofit en het aanhouden van de MVP structuur. Zeker de eerste weken zorgde dit voor veel extra code en moeite. Uiteindelijk ben ik voor zowel beide content met het resultaat en is dit iets wat zeker van pas gaat komen mocht ik verder gaan met Android.

De tweede, wat vervelendere, uitdaging is dat in het begin van week 4 de originele API (parkshark) incomplete data teruggaf. Waarbij ik namelijk in de eerste 3 weken zowel meters als garages terug kreeg bleken dit nu alleen nog maar garages te zijn. Hierdoor viel een groot deel van mijn functionaliteit weg en heb ik er uiteindelijk voor gekozen om via de RDW API locaties van parkeerpalen op te halen, deze API geeft echter in tegenstelling tot de eerste niets terug qua tijd/kosten/afstand. Om de gebruiker toch een indicatie te geven van parkeerkosten heb ik via polygon lines gebieden getekend op de google maps fragment. Deze polygon lines heb ik (helaas) hard-coded moeten implementeren vanuit een RDW open database set, denk hierbij aan 7 uur lang arrays copy-pasten...

Een extra uitdaging die zich hieruit vormde was het feit dat de nieuwe API gebruik maakte van SoQL queries, dit zijn queries waarbij een '?' wordt gebruikt in de URL. Momenteel bevind zich er een 'bug' in retrofit waarbij het niet mogelijk is om encoding van URLs uit te zetten en het voor mij dus niet mogelijk was parkeermeters op te halen rond de eigen locatie van de gebruiker, vandaar dat ik uiteindelijk gekozen heb om binnen een radius van 10km alle parkeermeters van centrum Amsterdam op te halen. Wanneer het wel mogelijk is om encoding uit te zetten is het mogelijk om veel gerichter de locaties van parkeermeters te tonen en kan het aantal getoonde meters om laag wat voor een betere gebruikerservaring zorgd.
(https://github.com/square/retrofit/issues/1199)

De derde uitdaging die als een rode draad door deze app loopt is performance;
- Origneel had ik gekozen om normale google maps fragments te tonen binnen de recyclerview, iets wat logisch zorgde voor erg veel stutter. De oplossing hiervoor was dus het gebruik van statische maps en handmatig de routefunctie in GoogleMaps starten.
- het ophalen van data splits ik nu tussen de twee activities om twee redenen; het doorsturen van alle parkeermeter locaties (1000+ items) tussen intents via serialized items is erg zwaar, en doordat het asynchroon gebeurd kan het prima tijdens het starten van de BaseActivity en scheelt tijd tijdens de eerste activity.
- Het switchen tussen fragments was in het begin erg zwaar, oa door het onthouden of opnieuw maken van de maps activity. De uiteindelijk gekozen oplossing was de MapsFragment nooit af te sluiten en de (veel lichtere) RouteListFragment hier gewoon voor te plaatsen.
- het tonen/aanpassen van heel veel markers i.c.m. de parkeerzones, uiteindelijk heeft Google Maps Utils me hierbij geholpen. Ook dit bracht uiteraard een uitdaging mee, inclusief het handmatig moeten updaten van de playservices door bugs aan de kant van google (https://code.google.com/p/gmaps-api-issues/issues/detail?id=9765)

Al met al hebben deze uitdagingen er wel voor gezorgd dat het eindresultaat in mijn opzicht een verbetering is t.o.v het origele idee.

###Referenties

#####Libraries
Retrofit (https://github.com/square/retrofit)
- Netwerk RestClient
- Ondersteunende libraries voor het uitpakken van JSON en URL encoding zijn OKHTTP en GSON

Dagger (http://square.github.io/dagger/)
- 'Dependancy injector', gebruikt voor het binden van verschillende lagen

Butterknife (https://github.com/JakeWharton/butterknife)
- Gebruikt voor het binden van views

RippleBackground(https://github.com/skyfishjy/android-ripple-background)
- Animatie beginscherm

Google Map Utils(https://github.com/googlemaps/android-maps-utils)
- Gebruikt voor marker clustering

Picasso(http://square.github.io/picasso/)
- Erg sterke library voor het ophalen (en cachen) van images

#####API
- http://divvapi.parkshark.nl/
- https://opendata.rdw.nl/resource/e6d2-rh45.json

#####Data
- https://opendata.rdw.nl/Parkeren/Open-Data-Parkeren-KAART-PARKEERGEBIEDEN/7sqe-3mca
- https://opendata.rdw.nl/Parkeren/Open-Data-Parkeren-GEOMETRIE-GEBIED/nsk3-v9n7
- https://www.amsterdam.nl/parkeren-verkeer/parkeertarieven/

#####Honorable mention
www.stackoverflow.com




















