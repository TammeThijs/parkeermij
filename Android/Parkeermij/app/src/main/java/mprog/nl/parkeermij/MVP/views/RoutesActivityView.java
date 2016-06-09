package mprog.nl.parkeermij.MVP.views;

import java.util.List;

import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface RoutesActivityView {
    void SetMapLocation(LocationObject locationObject);
    void setRecycerData(List<RouteObject> routeList);
}
