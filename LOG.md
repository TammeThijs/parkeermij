#####day1
- Startup
- Idee schaven
- Proposal maken
- Android studio prepareren

#####day2
- Design document
- Opzetten project binnen android studio
- MVP (dagger) initialiseren
- Informatie opzoeken mbt googlemaps API

#####day3
- Design document afgemaakt
- Standup
- Google maps geïmplementeerd

#####day4 
- Locatie bepaling dmv van GPS
- Locatie weergeven in google maps
- Locatie (gps) checks

#####day5
- Code cleanup
- Meeting op UvA

#####day6
- Retrofit geïmplementeerd
- JSON opgehaald (als string)
- API verder onderzoeken

#####day7 
- JSON omgezet naar objecten.
- MVP logica gecombineerd met retrofit
- Data via intent meesturen naar tweede activity

#####day8
- RoutesActivity
- Data ophalen uit intent
- Opzet Recyclerview

#####day9
- Recyclerview geïmplementeerd
- Static google maps in listitem
- OnClick listitem geeft id, transition, view en locationObject mee.
- Laat route zien (rechte lijn...)

#####day10
- Presentaties

#####day11
- Doet nu API request op basis van eigen locatie ipv hardcoded.
- Setup navigationdrawer

#####day12 
- switched naar gebruik van fragments
- maps fragment toegevoegd waar parkeerlocaties op worden getoond

#####day13
- fragments afgemaakt
- menu toegevoegd met opties per fragment
- gekeken naar lifecycle fragment ivm behoud van map

#####day14
- afwezig ivm zaken familie

#####day15
- presentaties
- API toont kuren, data incompleet

#####day16
- API geeft geen meters meer terug, alleen garages
- onderzoeken welke veranderingen nu in de app moeten worden gemaakt om deze toch interessant te houden
- gekozen om via (hardcoded!) google maps parkeerzones te laten zien binnen amsterdam
- Dataset RDW MAP: https://opendata.rdw.nl/Parkeren/Open-Data-Parkeren-KAART-PARKEERGEBIEDEN/7sqe-3mca
- Dataset RDW Polygon points: https://opendata.rdw.nl/Parkeren/Open-Data-Parkeren-GEOMETRIE-GEBIED/nsk3-v9n7
- Tariefen amsterdam: https://www.amsterdam.nl/parkeren-verkeer/parkeertarieven/
- Stadsdelen hardcoded laten zien op maps
- Erg veel performance issues door deze aanpassing, opzoek naar oplossingen om de app 'smooth' te laten aanvoelen voor gebruiker

#####day17
- Aantal performance changes gemaakt
- Oplossing gevonden voor behouden van map fragment wanneer een ander fragment wordt opgevraagd (deze wordt er nu overheen gelegd ipv replaced)
- Een nieuwe API gebruikt om parkeerpalen op te halen, helaas bevat deze alleen coordinaten en geen informatie qua prijs/tijden etc
- Models voor deze nieuwe meters aangemaakt 

#####day18
- Erg veel problemen performance google maps
- Google maps util uitgezocht en geïmplementeerd
- Marker clustering toegevoegd

######day19
- bijschaven functies app
- sorteer functie bij routes geïmplementeerd
- menu's verschillen nu a.d.h.v welk fragment getoond word
- legenda gemaakt in navigation drawer
- hiervoor shapes in xml getekend
- report

#####day20
- Eindpresentatie




