package mprog.nl.parkeermij.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mprog.nl.parkeermij.models.RouteList;
import mprog.nl.parkeermij.network.serializers.CustomDeserializer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manager class that handles netwerk connections
 */
public class ApiManager {

    private static final String TAG = "APImanager";

    // Initialise GSON deserialiser
    private static final Gson gson = new GsonBuilder()
            // Custom deserializer when working with RouteList.class
            .registerTypeAdapter(RouteList.class, new CustomDeserializer<RouteList>())
            .create();

    // OkHttpClient used for connection
    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    // combining all settings for each base-url
    private static final Retrofit PARK_ADAPTER = new Retrofit.Builder()
            .baseUrl("http://divvapi.parkshark.nl")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private static final Retrofit PARKMETER_ADAPTER = new Retrofit.Builder()
            .baseUrl("https://opendata.rdw.nl")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    private static final ParkService PARK_SERVICE = PARK_ADAPTER.create(ParkService.class);
    private static final MeterService METER_SERVICE = PARKMETER_ADAPTER.create(MeterService.class);

    public static ParkService getParkService() {
        return PARK_SERVICE;
    }
    public static MeterService getMeterService(){
        return METER_SERVICE;
    }
}
