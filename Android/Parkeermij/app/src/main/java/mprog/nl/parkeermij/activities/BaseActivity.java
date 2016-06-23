package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.MVP.presenters.BaseActivityPresenter;
import mprog.nl.parkeermij.MVP.views.BaseActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.dagger.components.DaggerRoutesActivityComponent;
import mprog.nl.parkeermij.dagger.modules.RoutesActivityModule;
import mprog.nl.parkeermij.fragments.MapsFragment;
import mprog.nl.parkeermij.fragments.RoutesListFragment;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;

public class BaseActivity extends AppCompatActivity implements BaseActivityView,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "BaseActivity";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";
    public static final String METERS = "meters";

    private List<RouteObject> mRouteObjects;
    private List<LatLng> mMeterObjects;
    private LocationObject mLocationObject;
    private RoutesListFragment mRoutesListFragment;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    BaseActivityPresenter mPresenter;

    public static Intent newIntent(Context context, Location location, List<RouteObject> routeObjects) {
        Intent intent = new Intent(context, BaseActivity.class);
        Bundle extras = new Bundle();

        LocationObject mLocation = new LocationObject(location.getLatitude(),
                location.getLongitude());

        extras.putSerializable(LOCATION, mLocation);
        extras.putSerializable(ROUTES, (Serializable) routeObjects);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_drawer_wrapper);
        ButterKnife.bind(this);

        initDependencies();
        init();
    }

    private void initDependencies() {
        DaggerRoutesActivityComponent.builder()
                .routesActivityModule(new RoutesActivityModule(this))
                .build()
                .inject(this);
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mPresenter.init(getIntent());
    }

    @Override
    public void showMap() {

        showOverflowMenu(true);
        if (getCurrentFragment() instanceof RoutesListFragment) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mRoutesListFragment)
                    .commit();
        } else {
            Fragment mapsFragment = MapsFragment.newInstance(this, mLocationObject, mRouteObjects,
                    mMeterObjects);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mapsFragment)
                    .commit();
        }
    }

    @Override
    public void showRoutes() {
        showOverflowMenu(false);
        mRoutesListFragment = RoutesListFragment.newInstance(mRouteObjects, mLocationObject);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, mRoutesListFragment)
                .commit();
    }

    @Override
    public void setData(List<RouteObject> routeList, LocationObject locationObject) {
        mRouteObjects = routeList;
        mLocationObject = locationObject;
    }


    /**
     * Load list with meters into fragment
     *
     * @param meters
     */
    @Override
    public void setMeterdata(List<MeterObject> meters) {
        List<LatLng> temp = new ArrayList<>();

        for (MeterObject meter : meters) {
            String[] coordinates = meter.getCoordinates().getCoordinates();
            temp.add(new LatLng(Double.parseDouble(coordinates[1]),
                    Double.parseDouble(coordinates[0])));
        }
        mMeterObjects = temp;
        showMap(); // load map if all data is gathered
    }

    /**
     * return active fragment in baseactivity
     *
     * @return
     */
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    /**
     * Override onBackPressed to close drawer if opened
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Toolbar menu ItemSelected. Settings are stored in sharedprefs.     *
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_settings), 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (item.getItemId()) {
            case R.id.garages:
                editor.putBoolean(getString(R.string.show_garages), item.isChecked());
                break;
            case R.id.meters:
                editor.putBoolean(getString(R.string.show_meters), item.isChecked());
                break;
            case R.id.afstand:
                editor.putBoolean(getString(R.string.sort_distance), true);
                break;
            case R.id.prijs:
                editor.putBoolean(getString(R.string.sort_distance), false);
            default:
                break;
        }
        editor.apply();

        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof MapsFragment) {
            ((MapsFragment) currentFragment).setMarkers();
        }
        if (currentFragment instanceof RoutesListFragment) {
            ((RoutesListFragment) currentFragment).filterList();

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // NavigationView Onclick.
        switch (item.getItemId()) {
            case R.id.maps:
                // only show map if current fragment isnt map
                if (getCurrentFragment() instanceof RoutesListFragment) {
                    showMap();
                }
                break;
            case R.id.routes:
                // only show routes if current fragment isnt routes
                if (getCurrentFragment() instanceof MapsFragment) {
                    showRoutes();
                }
                break;
            default:
                break;
        }

        // close drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_settings), 0);

        boolean isCheckedMeter = settings.getBoolean(getString(R.string.show_meters), false);
        boolean isCheckedGarage = settings.getBoolean(getString(R.string.show_garages), false);
        boolean isDistanceSort = settings.getBoolean(getString(R.string.sort_distance), false);

        MenuItem mItemMeters = menu.findItem(R.id.meters);
        mItemMeters.setChecked(isCheckedMeter);

        MenuItem mItemGarages = menu.findItem(R.id.garages);
        mItemGarages.setChecked(isCheckedGarage);

        if(isDistanceSort){
            MenuItem mItemDistance = menu.findItem(R.id.afstand);
            mItemDistance.setChecked(true);
        } else {
            MenuItem mItemPrijs = menu.findItem(R.id.prijs);
            mItemPrijs.setChecked(true);
        }



        return true;
    }

    public void showOverflowMenu(boolean showMenu) {
        if (mToolbar.getMenu() != null)
            mToolbar.getMenu().setGroupVisible(R.id.mapsmenu, showMenu);
        mToolbar.getMenu().setGroupVisible(R.id.routesmenu, !showMenu);
    }
}
