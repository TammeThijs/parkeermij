package mprog.nl.parkeermij.MVP.interactors.impl;

import android.util.Log;

import java.util.List;

import mprog.nl.parkeermij.MVP.interactors.StartUpActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;
import mprog.nl.parkeermij.models.RouteList;
import mprog.nl.parkeermij.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tamme on 31-5-2016.
 */
public class StartUpActivityInteractorImpl implements StartUpActivityInteractor {

    private ResponseListener<List<RouteObject>> mRouteResponseListener;
    private ResponseListener<List<MeterObject>> mMeterResponseListener;
    private RouteList mList;
    public static final String TAG = "Netwerk";

    @Override
    public void getRoutes(ResponseListener<List<RouteObject>> listener, LocationObject
            locationObject) {

        mRouteResponseListener = listener;

        Call<RouteList> call = ApiManager.getParkService().getGarages(String.valueOf(locationObject
                .getLatitude()),String.valueOf(locationObject.getLongitude()));
        call.enqueue(new Callback<RouteList>() {
            @Override
            public void onResponse(Call<RouteList> call, Response<RouteList> response) {
                if (response.isSuccessful()) {
                    mList = new RouteList(response.body().getRouteObjectList());
                    mRouteResponseListener.success(mList.getRouteObjectList());
                } else {
                    mRouteResponseListener.fail("Error getting routes: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<RouteList> call, Throwable t) {

                Log.d(TAG, "onFailure: FAILURE  " + t.getMessage());
            }
        });
    }

    @Override
    public void getParkMeters(ResponseListener<List<MeterObject>> listener, LocationObject locationObject) {

        mMeterResponseListener = listener;

        Call<List<MeterObject>> call = ApiManager.getParkService().getMeters();
        call.enqueue(new Callback<List<MeterObject>>() {
            @Override
            public void onResponse(Call<List<MeterObject>> call, Response<List<MeterObject>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse meter: SUCCES" + response.body().size());
                } else {
                    Log.d(TAG, "onResponse meter: FAILURE");
                }
            }

            @Override
            public void onFailure(Call<List<MeterObject>> call, Throwable t) {
                Log.d(TAG, "onFailure: FAILURE  " + t.getMessage());
            }
        });
    }
}
