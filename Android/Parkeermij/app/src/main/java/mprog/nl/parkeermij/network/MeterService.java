package mprog.nl.parkeermij.network;

import java.util.List;

import mprog.nl.parkeermij.models.MeterObject;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Tamme on 21-6-2016.
 */
public interface MeterService {

    @GET("/resource/gni9-a379.json")
    Call<List<MeterObject>> getMeters(

    );
}
