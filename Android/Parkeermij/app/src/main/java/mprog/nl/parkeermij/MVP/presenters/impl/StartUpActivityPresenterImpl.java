package mprog.nl.parkeermij.MVP.presenters.impl;

import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.interactors.StartUpActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.MVP.presenters.StartUpActivityPresenter;
import mprog.nl.parkeermij.MVP.views.StartUpActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 31-5-2016.
 */
public class StartUpActivityPresenterImpl implements StartUpActivityPresenter {

    public static final String TAG = "MainPresenter";

    private StartUpActivityView mView;
    private StartUpActivityInteractor mInteractor;

    @Inject
    public StartUpActivityPresenterImpl(StartUpActivityView view, StartUpActivityInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onClick(int id) {
        switch (id) {
            case R.id.location:
                mView.startRoutes();
                break;
            default:
                break;
        }
    }

    @Override
    public void getData(LocationObject locationObject) {
        mInteractor.getRoutes(mRouteResponseListener, locationObject);
        mInteractor.getParkMeters(mMeterResponseListener, locationObject);
    }

    private ResponseListener<List<RouteObject>> mRouteResponseListener = new ResponseListener<List<RouteObject>>() {
        @Override
        public void success(List<RouteObject> routeObjects) {
            mView.startRoutesActivity(routeObjects, null);
        }

        @Override
        public void fail(String error) {
            mView.toggleSnackbar("Fout bij ophalen data");
        }
    };

    private ResponseListener<List<MeterObject>> mMeterResponseListener = new ResponseListener<List<MeterObject>>() {
        @Override
        public void success(List<MeterObject> response) {
            mView.startRoutesActivity(null, response);
        }

        @Override
        public void fail(String error) {
            mView.toggleSnackbar("Fout bij ophalen data");
        }
    };
}
