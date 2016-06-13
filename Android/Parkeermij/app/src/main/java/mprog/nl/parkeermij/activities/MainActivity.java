package mprog.nl.parkeermij.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.skyfishjy.library.RippleBackground;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.MVP.presenters.MainActivityPresenter;
import mprog.nl.parkeermij.MVP.views.MainActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.dagger.components.DaggerMainActivityComponent;
import mprog.nl.parkeermij.dagger.modules.MainActivityModule;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

public class MainActivity extends AppCompatActivity implements MainActivityView,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MainActivity";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int GPS_CHECK = 999;

    public LocationManager mLocManager;
    private Boolean mLocationEnabled;
    private Boolean isRipple = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    @BindView(R.id.location)
    FloatingActionButton mLocationButton;

    @BindView(R.id.ripple)
    RippleBackground mRippleBackground;

    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    @Inject
    MainActivityPresenter mPresenter;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initDependencies();
        gpsCheck();

    }

    private void initDependencies() {
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    private void init() {
        mLocationButton.setOnClickListener(this);
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Build Google API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    @Override
    public void toggleRipple() {
        Log.d(TAG, "toggleRipple: CALLED");
        if (!isRipple) {
            mRippleBackground.startRippleAnimation();
            isRipple = true;
        } else {
            mRippleBackground.stopRippleAnimation();
            isRipple = false;
        }
    }

    private void gpsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Voor deze functie moet uw GPS aanstaan, wilt u deze nu aanzetten?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(
                                new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                GPS_CHECK);
                    }
                })
                .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void gpsCheck() {
        if (!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationButton.setAlpha(.5f);
            mLocationEnabled = false;
        } else {
            mLocationEnabled = true;
            mLocationButton.setAlpha(1f);
        }
    }

    @Override
    public void startRoutesActivity(List<RouteObject> routeObjects) {
        Intent intent = RoutesActivity.newIntent(this, mLocation, routeObjects);
        startActivity(intent);
    }

    @Override
    public void toggleSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        if (isNetworkAvailable()) { // internet check
            if (!isRipple) { // check if logic is in progress
                mPresenter.onClick(v.getId());
            }
        } else {
            toggleSnackbar("Er is geen internetverbinding");
        }
    }

    @Override
    public void startRoutes() {
        // user might be a dick and disabled GPS after application start
        gpsCheck();

        if (!mLocationEnabled) {
            gpsAlert();
        } else if (mLocation == null) {
            toggleRipple();
            mGoogleApiClient.connect();
        } else {
            if (!isRipple) {
                toggleRipple();
            }
            mPresenter.getData(new LocationObject(mLocation.getLatitude(), mLocation.getLongitude()));
        }
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_CHECK) {
            gpsCheck();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // TODO testen op 6.0+ device
            Toast.makeText(MainActivity.this, "Geen permissie", Toast.LENGTH_SHORT).show();

        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);
            } else {
                startRoutes();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // empty
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "LocationObject services connection failed: " +
                    connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "onLocationChanged: CALLED -> lat: "+mLocation.getLatitude());

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        if (isRipple) {
            toggleRipple();
        }
    }
}
