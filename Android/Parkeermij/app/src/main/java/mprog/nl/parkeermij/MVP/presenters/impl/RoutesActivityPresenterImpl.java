package mprog.nl.parkeermij.MVP.presenters.impl;

import android.content.Intent;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.interactors.RoutesActivityInteractor;
import mprog.nl.parkeermij.MVP.presenters.RoutesActivityPresenter;
import mprog.nl.parkeermij.MVP.views.RoutesActivityView;
import mprog.nl.parkeermij.activities.RoutesActivity;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class RoutesActivityPresenterImpl implements RoutesActivityPresenter {

    private RoutesActivityView mView;
    private RoutesActivityInteractor mInteractor;

    @Inject
    public RoutesActivityPresenterImpl(RoutesActivityView view, RoutesActivityInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void init(Intent intent) {
        LocationObject savedLocation = null;
        if (intent.hasExtra(RoutesActivity.LOCATION)) {
            savedLocation = (LocationObject) intent.getExtras().getSerializable(RoutesActivity.LOCATION);
        }
        if (intent.hasExtra(RoutesActivity.ROUTES) && savedLocation != null) {
            List<RouteObject> savedRouteObjects = (List<RouteObject>) intent.getExtras().getSerializable(RoutesActivity.ROUTES);
            mView.setRecycerData(savedRouteObjects, savedLocation);
        }
    }

    @Override
    public void startMap(LocationObject location, RouteObject routeObject, View view, String transition) {
        mView.startMap(location, routeObject, view,
                transition);
    }
}
