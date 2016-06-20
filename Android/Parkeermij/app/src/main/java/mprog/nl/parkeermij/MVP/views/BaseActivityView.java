package mprog.nl.parkeermij.MVP.views;

import java.util.List;

import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface BaseActivityView {
    void showMap(List<RouteObject> routeList, LocationObject locationObject);
    LocationObject getLocation();
}
