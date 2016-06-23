package mprog.nl.parkeermij.network;

import java.util.List;

import mprog.nl.parkeermij.models.MeterObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Tamme on 21-6-2016.
 */
public interface MeterService {

    // get all meters in radius 10k from center Amsterdam
    @Headers("X-App-Token: kFWFTU7J76V9VjmpOwCyXf1z4") // api token
    @GET("/resource/gni9-a379.json?$where=within_circle(location, 52.374507, 4.896554, 10000)")
    Call<List<MeterObject>> getMeters(
    );
}
