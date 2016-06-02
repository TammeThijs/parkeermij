package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.LocationObject;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback{

    public static final String TAG = "LocationActivity";
    public static final String LOCATION = "location";
    private GoogleMap mMap;
    private LocationObject mLocationObject;


    public static Intent newIntent(Context context, LocationObject location) {
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
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

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        // map settings
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //current position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLocationObject.getLatitude(), mLocationObject.getLongitude()), 16));

        mMap.addMarker(new MarkerOptions().position(
                new LatLng(mLocationObject.getLatitude(), mLocationObject.getLongitude())).icon(
                BitmapDescriptorFactory.defaultMarker()));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
