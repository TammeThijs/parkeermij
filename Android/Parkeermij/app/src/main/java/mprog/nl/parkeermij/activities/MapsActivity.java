package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "MapActivity";
    public static final String LOCATION = "location";
    public static final String ROUTE = "routes";
    public static final String TRANSITION = "transition";

    @BindView(R.id.content)
    LinearLayout mContent;

    private GoogleMap mMap;
    private LocationObject startLocation;
    private LocationObject parkLocation;
    private RouteObject savedRouteObject;



    public static Intent newIntent(Context context, LocationObject location, RouteObject routeObject,
                                   String transition) {
        Intent intent = new Intent(context, MapsActivity.class);
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
        extras.putSerializable(ROUTE, routeObject);
        extras.putString(TRANSITION, transition);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        String transition =  extras.getString(TRANSITION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mContent.setTransitionName(transition);
        }
        Intent intent = getIntent();
        if(intent.hasExtra(MapsActivity.LOCATION)){
            startLocation = (LocationObject) intent.getExtras().getSerializable(RoutesActivity.LOCATION);
        }
        if (intent.hasExtra(MapsActivity.ROUTE)){
            savedRouteObject = (RouteObject) intent.getExtras().getSerializable(RoutesActivity.ROUTES);

            assert savedRouteObject != null;
            parkLocation = savedRouteObject.getGPSLocation();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()),
                        new LatLng(parkLocation.getLatitude(), parkLocation.getLongitude()))
                .geodesic(false));
//        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.addMarker(new MarkerOptions().position(new LatLng(startLocation.getLatitude(),
                startLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker()));
        mMap.addMarker(new MarkerOptions().position(new LatLng(parkLocation.getLatitude(),
                parkLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker()));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parkLocation.getLatitude(),
                parkLocation.getLongitude()), 15));
    }
}
