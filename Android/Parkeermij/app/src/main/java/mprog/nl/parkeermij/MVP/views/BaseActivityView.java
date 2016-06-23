package mprog.nl.parkeermij.MVP.views;

import java.util.List;

import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface BaseActivityView {
    void setData(List<RouteObject> routeList, LocationObject locationObject);
    void showMap();
    void showRoutes();
    void setMeterdata(List<MeterObject> meters, boolean succes);
    void updateMenu(boolean showMenu);
    void applyMenuChange();
}
