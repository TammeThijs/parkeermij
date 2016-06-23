package mprog.nl.parkeermij.MVP.interactors.impl;

import java.util.List;

import mprog.nl.parkeermij.MVP.interactors.StartUpActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteList;
import mprog.nl.parkeermij.models.RouteObject;
import mprog.nl.parkeermij.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tamme on 31-5-2016.
 */
public class StartUpActivityInteractorImpl implements StartUpActivityInteractor {

    private ResponseListener<List<RouteObject>> mRouteResponseListener;
    private RouteList mList;

    @Override
    public void getRoutes(ResponseListener<List<RouteObject>> listener, LocationObject
            locationObject) {
        // set listener
        mRouteResponseListener = listener;

        // call asychronous
        Call<RouteList> call = ApiManager.getParkService().getGarages(String.valueOf(locationObject
                .getLatitude()), String.valueOf(locationObject.getLongitude()));
        call.enqueue(new Callback<RouteList>() {
            @Override
            public void onResponse(Call<RouteList> call, Response<RouteList> response) {
                if (response.isSuccessful()) {
                    mList = new RouteList(response.body().getRouteObjectList());
                    mRouteResponseListener.success(mList.getRouteObjectList()); // call succes from listener
                } else {
                    mRouteResponseListener.fail("Error getting data"); // call fail from listener
                }
            }

            @Override
            public void onFailure(Call<RouteList> call, Throwable t) {
                mRouteResponseListener.fail("Error getting data: "+t.getMessage()); // call fail from listener
            }
        });
    }
}
