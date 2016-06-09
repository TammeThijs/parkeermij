package mprog.nl.parkeermij.MVP.views;

import java.util.List;

import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 31-5-2016.
 */
public interface MainActivityView {
    void toggleRipple();
    void startRoutes();
    void gpsCheck();
    void startRoutesActivity(List<RouteObject> routeObjects);
    void toggleSnackbar(String message);
    boolean isNetworkAvailable();
}
