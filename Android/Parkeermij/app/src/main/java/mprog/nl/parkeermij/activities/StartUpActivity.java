package mprog.nl.parkeermij.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
import mprog.nl.parkeermij.MVP.presenters.StartUpActivityPresenter;
import mprog.nl.parkeermij.MVP.views.StartUpActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.dagger.components.DaggerMainActivityComponent;
import mprog.nl.parkeermij.dagger.modules.MainActivityModule;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;

public class StartUpActivity extends AppCompatActivity implements StartUpActivityView,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "StartUpActivity";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int GPS_CHECK = 999;

    private LocationManager mLocManager;
    private Boolean mLocationEnabled;
    private Boolean isRipple = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private List<RouteObject> mRouteObjects = null;
    private List<MeterObject> mMeterObjects = null;

    @BindView(R.id.location)
    FloatingActionButton mLocationButton;

    @BindView(R.id.ripple)
    RippleBackground mRippleBackground;

    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    @Inject
    StartUpActivityPresenter mPresenter;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StartUpActivity.class);
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

        startRoutes();
    }

    /**
     * Used for feedback to user that app is handling data
     */
    @Override
    public void toggleRipple() {
        if (!isRipple) {
            mRippleBackground.startRippleAnimation();
            isRipple = true;
        } else {
            mRippleBackground.stopRippleAnimation();
            isRipple = false;
        }
    }

    /**
     * Allert message if GPS is currently not enabled or available.
     */
    private void gpsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Voor de huidige locatie moet uw GPS aanstaan, wilt u deze nu aanzetten?")
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

    /**
     * Checks is GPS is available
     */
    @Override
    public void gpsCheck() {
        if (!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Dissable button change layout so that user knows its unavailable
            mLocationButton.setAlpha(.5f);
            mLocationEnabled = false;
        } else {
            // Enable button
            mLocationEnabled = true;
            mLocationButton.setAlpha(1f);
        }
    }

    @Override
    public void startRoutesActivity(@Nullable List<RouteObject> routeObjects,
                                    @Nullable List<MeterObject> meterObjects) {

        if(routeObjects != null){
            mRouteObjects = routeObjects;
        }

        if(meterObjects != null){
            mMeterObjects = meterObjects;
        }

        if(mMeterObjects != null && mRouteObjects != null){
            Intent intent = BaseActivity.newIntent(this, mLocation, mRouteObjects, mMeterObjects);
            startActivity(intent);
        }

    }

    /**
     * Shows snackbar with custom message
     * @param message
     */
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
            toggleSnackbar(getString(R.string.string_no_internet));
        }
    }

    @Override
    public void startRoutes() {
        // double check GPS, user might have turned it off after initial check
        gpsCheck();

        if (!mLocationEnabled) {
            gpsAlert();
        } else if (mLocation == null) { // check if location exists
            toggleRipple();
            mGoogleApiClient.connect();
        } else {
            if (!isRipple) {
                toggleRipple();
            }
            mPresenter.getData(new LocationObject(mLocation.getLatitude(), mLocation.getLongitude()));
        }
    }

    /**
     * Method that checks if device has network acces.
     * Source: stackoverflow.
     * @return boolean
     */
    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Catches result from GPS activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check result
        if (requestCode == GPS_CHECK && resultCode == Activity.RESULT_OK ) {
            gpsCheck();
        }
    }

    /**
     * Is called after connection with GoogleApiClient, checks permissions and if the GPS
     * returned a valid location.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

        } else {
            // try to populate location object
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                // keep asking API for location when location is null
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);
            } else {
                // start next activity
                startRoutes();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // required method
    }

    /**
     * Called after GoogleAPIClient failed
     * Source: http://stackoverflow.com/questions/17811720/googleplayservicesutil-error-dialog-button-does-nothing
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // test connection
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            // Snackbar fail connection GoogleApiClient
            toggleSnackbar(connectionResult.getErrorMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // permissioncheck
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
        Log.d(TAG, "onLocationChanged: CALLED");
        // update locationObject
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: CALLED");
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
