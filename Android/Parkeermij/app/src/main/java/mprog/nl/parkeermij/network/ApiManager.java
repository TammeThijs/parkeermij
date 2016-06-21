package mprog.nl.parkeermij.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mprog.nl.parkeermij.models.RouteList;
import mprog.nl.parkeermij.network.serializers.CustomDeserializer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String TAG = "APImanager";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RouteList.class, new CustomDeserializer<RouteList>())
            .create();

    Interceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    private static final OkHttpClient client = new OkHttpClient.Builder().build();

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

    public static ParkService getParkService() {
        return PARK_SERVICE;
    }
}
