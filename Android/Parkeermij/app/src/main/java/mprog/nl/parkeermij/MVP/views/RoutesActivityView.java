package mprog.nl.parkeermij.MVP.views;

import android.view.View;

import java.util.List;

import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface RoutesActivityView {
    void setRecycerData(List<RouteObject> routeList, LocationObject locationObject);
    void startMap(LocationObject location, RouteObject routeObject, View view,
                  String transition);
}
