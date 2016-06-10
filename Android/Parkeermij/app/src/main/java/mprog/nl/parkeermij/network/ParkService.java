package mprog.nl.parkeermij.network;

import mprog.nl.parkeermij.models.RouteList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Tamme on 6-6-2016.
 */
public interface ParkService {

    @GET("/apitest.jsp?action=plan&to_lat=52.352590&to_lon=4.948865&dur=2&opt_routes=y&opt_routes_ret=n&opt_am=n&opt_rec=y")
    Call<RouteList> getTest();
}
