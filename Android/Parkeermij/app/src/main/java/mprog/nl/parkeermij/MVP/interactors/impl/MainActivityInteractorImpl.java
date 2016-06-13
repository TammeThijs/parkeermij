package mprog.nl.parkeermij.MVP.interactors.impl;

import android.util.Log;

import java.util.List;

import mprog.nl.parkeermij.MVP.interactors.MainActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;
import mprog.nl.parkeermij.models.RouteList;
import mprog.nl.parkeermij.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tamme on 31-5-2016.
 */
public class MainActivityInteractorImpl implements MainActivityInteractor {

    private ResponseListener<List<RouteObject>> mContactsResponseListener;
    private RouteList mList;
    public static final String TAG = "Netwerk";

    @Override
    public void getRoutes(ResponseListener<List<RouteObject>> listener, LocationObject
            locationObject) {

        mContactsResponseListener = listener;

        Call<RouteList> call = ApiManager.getParkService().getTest(String.valueOf(locationObject
                .getLatitude()),String.valueOf(locationObject.getLongitude()));
        call.enqueue(new Callback<RouteList>() {
            @Override
            public void onResponse(Call<RouteList> call, Response<RouteList> response) {
                if (response.isSuccessful()) {
                    mList = new RouteList(response.body().getRouteObjectList());
                    mContactsResponseListener.success(mList.getRouteObjectList());
                } else {
                    mContactsResponseListener.fail("Error getting routes: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<RouteList> call, Throwable t) {
                Log.d(TAG, "onFailure: FAILURE  " + t.getMessage());
            }
        });
    }
}
