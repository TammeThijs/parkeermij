package mprog.nl.parkeermij.MVP.presenters.impl;

import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.interactors.RoutesActivityInteractor;
import mprog.nl.parkeermij.MVP.presenters.RoutesActivityPresenter;
import mprog.nl.parkeermij.MVP.views.RoutesActivityView;
import mprog.nl.parkeermij.activities.RoutesActivity;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.Route;

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
        if(intent.hasExtra(RoutesActivity.LOCATION)){
            LocationObject savedLocation = (LocationObject) intent.getExtras().getSerializable(RoutesActivity.LOCATION);
            mView.SetMapLocation(savedLocation);
        }
        if (intent.hasExtra(RoutesActivity.ROUTES)){
            List<Route> savedRoutes = (List<Route>) intent.getExtras().getSerializable(RoutesActivity.ROUTES);
            // TODO load into list
        }
    }
}
