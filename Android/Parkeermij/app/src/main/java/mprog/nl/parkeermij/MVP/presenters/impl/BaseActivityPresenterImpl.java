package mprog.nl.parkeermij.MVP.presenters.impl;

import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.interactors.BaseActivityInteractor;
import mprog.nl.parkeermij.MVP.presenters.BaseActivityPresenter;
import mprog.nl.parkeermij.MVP.views.BaseActivityView;
import mprog.nl.parkeermij.activities.BaseActivity;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class BaseActivityPresenterImpl implements BaseActivityPresenter {

    private BaseActivityView mView;
    private BaseActivityInteractor mInteractor;

    @Inject
    public BaseActivityPresenterImpl(BaseActivityView view, BaseActivityInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void init(Intent intent) {
        LocationObject savedLocation = null;
        if (intent.hasExtra(BaseActivity.LOCATION)) {
            savedLocation = (LocationObject) intent.getExtras().getSerializable(BaseActivity.LOCATION);
        }
        if (intent.hasExtra(BaseActivity.ROUTES) && savedLocation != null) {
            List<RouteObject> savedRouteObjects = (List<RouteObject>) intent.getExtras().getSerializable(BaseActivity.ROUTES);
            mView.showMap(savedRouteObjects, savedLocation);
        }
    }
}
