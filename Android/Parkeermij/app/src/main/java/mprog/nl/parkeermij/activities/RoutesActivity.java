package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.presenters.RoutesActivityPresenter;
import mprog.nl.parkeermij.MVP.views.RoutesActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.dagger.components.DaggerRoutesActivityComponent;
import mprog.nl.parkeermij.dagger.modules.RoutesActivityModule;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.Route;

public class RoutesActivity extends AppCompatActivity implements OnMapReadyCallback,
        RoutesActivityView {

    public static final String TAG = "RoutesActivity";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";

    private GoogleMap mMap;
    private List<Route> mRoutes;

    @Inject
    RoutesActivityPresenter mPresenter;

    public static Intent newIntent(Context context, LocationObject location, List<Route> routes) {
        Intent intent = new Intent(context, RoutesActivity.class);
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
        extras.putSerializable(ROUTES, (Serializable) routes);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initDependencies();
        init();
    }

    private void initDependencies() {
        DaggerRoutesActivityComponent.builder()
                .routesActivityModule(new RoutesActivityModule(this))
                .build()
                .inject(this);
    }

    private void init(){
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // map settings
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mPresenter.init(getIntent());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void SetMapLocation(LocationObject locationObject) {
        //current position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(locationObject.getLatitude(), locationObject.getLongitude()), 15));

        mMap.addMarker(new MarkerOptions().position(
                new LatLng(locationObject.getLatitude(), locationObject.getLongitude())).icon(
                BitmapDescriptorFactory.defaultMarker()));
    }
}
