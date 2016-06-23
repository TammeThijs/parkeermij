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
<img src="https://github.com/TammeThijs/parkeermij/blob/master/doc/Parkeermij%20Diagram.png" align="middle">

#####Design pattern
In dit project heb ik er voor gekozen om het Model View Controller (MVP) design patroon aan te houden. Ik was hier al wel eens eerder in aanraking mee gekomen bij het bouwen van een app, echter was dit de eerste keer dat ik dit compleet heb geïntegreerd binnen een zelfgemaakte app. Het principe van MVP rust op het beter bekende MVC binnen web development, echter zijn er wat unieke omstandigheden bij app development waardoor MVP in het leven is geroepen. De verschillende lagen dienen elk voor een eigen functie, in dit geval:
- View (Activities/Fragments) regelen 'front-end' handelingen zoals animaties, popups en het starten van een activity.
- Presenter (redelijk gelijk aan de controller) regelt alle logica, zoals user input en OnClick events
- Interactor, dit is de enige laag waar data requests gedaan mogen worden, bijvoorbeeld bij het ophalen van JSON etc.



