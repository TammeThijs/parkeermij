package mprog.nl.parkeermij.MVP.presenters;

import android.content.Intent;
import android.view.View;

import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface RoutesActivityPresenter {
    void init(Intent intent);
    void startMap(LocationObject location, RouteObject routeObject, View view,
                  String transition);
}
