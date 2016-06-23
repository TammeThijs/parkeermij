package mprog.nl.parkeermij.MVP.presenters.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.interactors.BaseActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.MVP.presenters.BaseActivityPresenter;
import mprog.nl.parkeermij.MVP.views.BaseActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.activities.BaseActivity;
import mprog.nl.parkeermij.fragments.MapsFragment;
import mprog.nl.parkeermij.fragments.RoutesListFragment;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
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
            mView.setData(savedRouteObjects, savedLocation);
        }
        mInteractor.getParkMeters(mMeterResponseListener, savedLocation);
    }

    @Override
    public void onNavigationItemSelected(int id, Fragment fragment) {

        switch (id) {
            case R.id.maps:
                // only show map if current fragment isnt map
                if (fragment instanceof RoutesListFragment) {
                    mView.showMap();
                }
                break;
            case R.id.routes:
                // only show routes if current fragment isnt routes
                if (fragment instanceof MapsFragment) {
                    mView.showRoutes();
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onOptionsItemSelected(int id, Context context, boolean isChecked) {
        SharedPreferences settings = context.getSharedPreferences(context
                .getString(R.string.shared_settings), 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (id) {
            case R.id.garages:
                editor.putBoolean(context.getString(R.string.show_garages), isChecked);
                break;
            case R.id.meters:
                editor.putBoolean(context.getString(R.string.show_meters), isChecked);
                break;
            case R.id.afstand:
                editor.putBoolean(context.getString(R.string.sort_distance), true);
                break;
            case R.id.prijs:
                editor.putBoolean(context.getString(R.string.sort_distance), false);
            default:
                break;
        }

        editor.apply(); // save changes
        mView.applyMenuChange();
    }


    private ResponseListener<List<MeterObject>> mMeterResponseListener = new ResponseListener<List<MeterObject>>() {
        @Override
        public void success(List<MeterObject> response) {
            mView.setMeterdata(response, true);
        }

        @Override
        public void fail(String error) {
            mView.setMeterdata(null, false);
        }
    };
}
