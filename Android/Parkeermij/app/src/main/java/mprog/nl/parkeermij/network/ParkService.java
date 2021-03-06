package mprog.nl.parkeermij.network;

import mprog.nl.parkeermij.models.RouteList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tamme on 6-6-2016.
 */
public interface ParkService {

    // get garages from parkshark api depending on location
    @GET("/rest/plan?dur=2&opt_routes=y&opt_routes_ret=n&opt_am=y&opt_rec=y")
    Call<RouteList> getGarages(
            @Query("to_lat") String lat, // latitude
            @Query("to_lon") String lon // longitude
    );
}
