package mprog.nl.parkeermij.MVP.interactors.impl;

import java.util.List;

import mprog.nl.parkeermij.MVP.interactors.BaseActivityInteractor;
import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tamme on 8-6-2016.
 */
public class BaseActivityInteractorImpl implements BaseActivityInteractor {

    private ResponseListener<List<MeterObject>> mMeterResponseListener;


    @Override
    public void getParkMeters(ResponseListener<List<MeterObject>> listener, LocationObject locationObject) {
        // set listener
        mMeterResponseListener = listener;

        // call asychronous
        Call<List<MeterObject>> call = ApiManager.getMeterService().getMeters();
        call.enqueue(new Callback<List<MeterObject>>() {
            @Override
            public void onResponse(Call<List<MeterObject>> call, Response<List<MeterObject>> response) {
                if (response.isSuccessful()) {
                    mMeterResponseListener.success(response.body()); // call succes from listener

                } else {
                    mMeterResponseListener.fail("Error getting data"); // call fail from listener
                }
            }

            @Override
            public void onFailure(Call<List<MeterObject>> call, Throwable t) {
                mMeterResponseListener.fail("Error getting data: "+t.getMessage()); // call fail from listener
            }
        });
    }
}
