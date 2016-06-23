package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import mprog.nl.parkeermij.dagger.components.DaggerBaseActivityComponent;
import mprog.nl.parkeermij.dagger.modules.BaseActivityModule;
import mprog.nl.parkeermij.fragments.MapsFragment;
import mprog.nl.parkeermij.fragments.RoutesListFragment;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.MeterObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Tamme Thijs
 * Base Activity, this activity is as base for inflating Maps and RouteList Fragment.
 */

public class BaseActivity extends AppCompatActivity implements BaseActivityView,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";

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


    /**
     * Static method to start BaseActivity from another activiy
     * @param context
     * @param location
     * @param routeObjects
     * @return
     */
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
        DaggerBaseActivityComponent.builder()
                .baseActivityModule(new BaseActivityModule(this))
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
        mNavigationView.setItemIconTintList(null); // custom colors required cause of legend
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mPresenter.init(getIntent());
    }

    /**
     * Loads MapsFragment
     */
    @Override
    public void showMap() {
        updateMenu(true);

        // Remove RouteListFragment if Mapsfragment already exists
        if (getCurrentFragment() instanceof RoutesListFragment) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mRoutesListFragment)
                    .commit();
        } else {
            // Init MapsFragment, only done once
            Fragment mapsFragment = MapsFragment.newInstance(mLocationObject, mRouteObjects,
                    mMeterObjects);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mapsFragment)
                    .commit();
        }
    }

    /**
     * Loads RouteList Fragment
     */
    @Override
    public void showRoutes() {
        updateMenu(false);
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
     * @param meters
     */
    @Override
    public void setMeterdata(@Nullable List<MeterObject> meters, boolean succes) {

        if (succes) {
            // convert to Latlng
            List<LatLng> temp = new ArrayList<>();

            for (MeterObject meter : meters) {
                String[] coordinates = meter.getCoordinates().getCoordinates();
                temp.add(new LatLng(Double.parseDouble(coordinates[1]),
                        Double.parseDouble(coordinates[0])));
            }
            mMeterObjects = temp;
        } else {
            Toast.makeText(BaseActivity.this, getString(R.string.rdw_api_fail), Toast.LENGTH_SHORT)
                    .show();
        }
        showMap(); // load map
    }

    /**
     * return active fragment in baseactivity
     * * @return
     */
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    /**
     * Override onBackPressed to custom behaviour
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // close drawer if open
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // if RouteListFragment is active go back to map
            if (getCurrentFragment() instanceof RoutesListFragment) {
                showMap();
                mNavigationView.getMenu().getItem(0).setChecked(true);
            } else {
                // MapsActivity is active, close app
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // feedback to user
        item.setChecked(!item.isChecked());

        // Handle logic in presenter
        mPresenter.onOptionsItemSelected(item.getItemId(), this, item.isChecked());

        return super.onOptionsItemSelected(item);
    }

    /**
     * If menu settings are changed refresh current fragment data.
     */
    @Override
    public void applyMenuChange() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof MapsFragment) {
            ((MapsFragment) currentFragment).setMarkers();
        }
        if (currentFragment instanceof RoutesListFragment) {
            ((RoutesListFragment) currentFragment).filterList();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // handle logic in presenter
        mPresenter.onNavigationItemSelected(item.getItemId(), getCurrentFragment());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Init toolbar options menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // load settings from sharedprefs
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_settings), 0);
        boolean isCheckedMeter = settings.getBoolean(getString(R.string.show_meters), false);
        boolean isCheckedGarage = settings.getBoolean(getString(R.string.show_garages), false);
        boolean isDistanceSort = settings.getBoolean(getString(R.string.sort_distance), false);

        // apply settings
        MenuItem mItemMeters = menu.findItem(R.id.meters);
        mItemMeters.setChecked(isCheckedMeter);

        MenuItem mItemGarages = menu.findItem(R.id.garages);
        mItemGarages.setChecked(isCheckedGarage);

        if (isDistanceSort) {
            MenuItem mItemDistance = menu.findItem(R.id.afstand);
            mItemDistance.setChecked(true);
        } else {
            MenuItem mItemPrijs = menu.findItem(R.id.prijs);
            mItemPrijs.setChecked(true);
        }

        return true;
    }

    @Override
    public void updateMenu(boolean showMenu) {
        if (mToolbar.getMenu() != null) {
            mToolbar.getMenu().setGroupVisible(R.id.mapsmenu, showMenu);
            mToolbar.getMenu().setGroupVisible(R.id.routesmenu, !showMenu);
        }
    }
}
