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

#####Design pattern
In dit project heb ik er voor gekozen om het Model View Controller (MVP) design patroon aan te houden. Ik was hier al wel eens eerder in aanraking mee gekomen bij het bouwen van een app, echter was dit de eerste keer dat ik dit compleet heb geïntegreerd binnen een zelfgemaakte app. Het principe van MVP rust op het beter bekende MVC binnen web development, echter zijn er wat unieke omstandigheden bij app development waardoor MVP in het leven is geroepen. De verschillende lagen dienen elk voor een eigen functie, in dit geval:
- View (Activities/Fragments) regelen 'front-end' handelingen zoals animaties, popups en het starten van een activity.
- Presenter (redelijk gelijk aan de controller) regelt alle logica, zoals user input en OnClick events
- Interactor, dit is de enige laag waar data requests gedaan mogen worden, bijvoorbeeld bij het ophalen van JSON etc.

Voor het 'binden' van deze lagen heb ik er voor gekozen om de Dagger library te gebruiken, voor elke activity kan ik dan een module + component opstellen waarbij ik met oa annotaties aan kan geven wat de relaties zijn tussen de activitie en achterliggende lagen.

#####Netwerk
Voor het ophalen van data heb ik gebruik gemaakt van Retrofit, een 'type-safe REST client'. Hoewel ik zelf nog nooit met retrofit had gewerkt (wel eens gezien) weet ik wel dat vrijwel alle bedrijven etc hier gebruik van maken en daarom wou ik dit graag leren tijdens dit project. Zeker bij de eerste API kostte dit erg veel tijd om goed geïmplementeerd te krijgen, wanneer het noodzakelijk was om een tweede API te gebruiken ging dit al vele malen sneller en ben ik blij dat ik hier voor gekozen heb.

#####Classes Overview

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

#####Short Explanation

In de eerste activity, de StartUpActivity, wordt voornamelijk data verzonden en opgehaald. Wanneer de activity start wordt er via GPS de huidige locatie van de gebruiker opgevraagd. Vervolgens wordt deze naar de ParkShark API gestuurd en de teruggegeven data omgezet in objecten. Tijdens de start, en bij het contact maken met de API wordt zowel de internet verbinding gecheckt als GPS. Mocht de GPS uitstaan dan krijgt de gebruiker een optie om deze aan te zetten. In geval van geen internet krijgt de gebruiker hier een bericht van. Mocht dit succesvol zijn dat wordt automatisch de BaseActivity gestart, in deze activity is dus als alles goed gaat geen input van de user nodig.
#
In de base activity zelf wordt de meegegeven data vanuit de startUpActivity opgehaald, en tergelijkertijd een nieuwe request gestuurd naar de API van het RDW om alle parkeerpalen op te halen binnen een 10km radius van centrum amsterdam. Dit gebeurd uiteraard allemaal asynchroon. Ook worden de menu items in de toolbar, en de navigationdrawer geïnitialiseerd. Aan de hand van welk van de twee fragments momenteel zichbaar is veranderen de functies van het toolbar menu; bij de mapsfragment is er de opties om meters en/of garages te tonen, in de routefragment kunnen deze gesorteerd worden op afstand of prijs. Deze settings worden opgeslagen door middel van sharedpreferences. Wanneer de data is opgehaald (succesvol of niet) wordt de MapsFragment geinflate.
#
De MapsFragment betreft de grootste functionaliteit voor de user van deze app. Nadat alle data is opgehaald uit de intent wordt hierin wordt de map geladen, parkeergebieden van amsterdam getekend en markers van garages en/of meters getoond. Een route naar een geselecteerde marker kan worden getoond door een icon binnen google maps zelf. Een erg grote uitdaging was dit zo soepel mogelijk te laten verlopen, al merkte ik snel dat dit erg veel vroeg van je mobiel. Meer hier over bij het kopje challenges.
#
De RouteListFragment laat de lijst van gevonden garages zien d.m.v een recyclerview. In de adapter kan deze m.b.v een comparator gesorteerd worden op prijs of afstand. De voorbeelden van locaties worden getoond door gebruik te maken van statische google images, die kunnen worden opgevraagd door het combineren van latitude en longitue tot een URL. Het tonen van deze images wordt gedaan door de library Picasso die hebt bij het asynchroon laden van images iets wat vrijwel noodzakelijk is bij gebruik van een list/recycler view.
#
Qua models spreekt het redelijk voor zich, naast de MeterObject en RouteObject was het noodzakelijk om een LocationObject te gebruiken voor het opslaan van de huidige locatie van de gebruiker. Dit omdat het model wat terug wordt gegeven door de GoogleClient niet serializable is en dus niet zonder problemen kan worden doorgegeven tussen activities. ClusterObject werd noodzakelijk door het gebruik van de Google Maps Utils waarbij custom markers alleen kunnen worden geplaatst door classes die ClustItem implementeren. 













