###Code

######activities
- landing page(main)
- confirm location (google maps, fragment)
- show routes
- show route on map (google maps, fragment)
- settings (optioneel)

######MVP
- Elke activitie is opgebouw vanuit een MVP (Model View Presenter) structuur;
	- View (interface voor activity)
	- Presenter (laag die logica afhandeld)
	- Interactor (laag die data afhandeld)
- deze lagen worden gebind mbv dagger door de components en modules

######models
- Routes (prijs, afstand, lat, long, etc)
- Profiel (optioneel)

######overig
- routes laten zien door middel van een recyclerview (dus ook adapter)
- JSON ophalen via http requests (REST)
- GPS locatie ophalen en gebruiken binnen google maps
- Route displayen in maps fragment

###Sketches
see doc directory

###API
- google maps API
- http://divvapi.parkshark.nl/

###Other sources
- Dagger 2
- Butterknife
- Retrofit
- &more
(Alle gebruikte libs zijn te vinden in gradle)

####Database
De huidige applicatie bevat geen database