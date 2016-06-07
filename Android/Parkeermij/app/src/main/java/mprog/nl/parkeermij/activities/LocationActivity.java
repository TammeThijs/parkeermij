package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.List;

import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.Route;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String TAG = "LocationActivity";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";
    private GoogleMap mMap;
    private LocationObject mLocationObject;
    private List<Route> mRoutes;


    public static Intent newIntent(Context context, LocationObject location, List<Route> routes) {
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
        extras.putSerializable(ROUTES, (Serializable) routes);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        mLocationObject = (LocationObject) extras.getSerializable(LOCATION);
        mRoutes = (List<Route>) extras.getSerializable(ROUTES);

        Log.d(TAG, "onCreate: routes size = " + mRoutes.size());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // map settings
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //current position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLocationObject.getLatitude(), mLocationObject.getLongitude()), 15));

        mMap.addMarker(new MarkerOptions().position(
                new LatLng(mLocationObject.getLatitude(), mLocationObject.getLongitude())).icon(
                BitmapDescriptorFactory.defaultMarker()));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
