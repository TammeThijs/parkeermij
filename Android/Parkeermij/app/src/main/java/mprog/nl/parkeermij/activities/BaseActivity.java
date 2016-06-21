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

import java.io.Serializable;
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
    private LocationObject mLocationObject;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Inject
    BaseActivityPresenter mPresenter;

    public static Intent newIntent(Context context, Location location, List<RouteObject> routeObjects,
                                   List<MeterObject> meterObjects) {
        Intent intent = new Intent(context, BaseActivity.class);
        Bundle extras = new Bundle();

        LocationObject mLocation = new LocationObject(location.getLatitude(),
                location.getLongitude());

        extras.putSerializable(LOCATION, mLocation);
        extras.putSerializable(ROUTES, (Serializable) routeObjects);
        extras.putSerializable(METERS, (Serializable) meterObjects);
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
    public void showMap(List<RouteObject> routeList, LocationObject locationObject) {

        mRouteObjects = routeList;
        mLocationObject = locationObject;

        Fragment mapsFragment = MapsFragment.newInstance(this, locationObject, routeList);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mapsFragment, MapsFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public LocationObject getLocation() {
        return mLocationObject;
    }

    public void showRoutes(List<RouteObject> routes) {

        Fragment routefragment = RoutesListFragment.newInstance(routes);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, routefragment, RoutesListFragment.FRAGMENT_TAG)
                .commit();
    }

    /**
     * return active fragment in baseactivity
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SharedPreferences settings = getSharedPreferences("settings", 0);

        boolean isCheckedMeter = settings.getBoolean("meterbox", false);
        boolean isCheckedGarage = settings.getBoolean("garagebox", false);

        MenuItem item = menu.findItem(R.id.meters);
        item.setChecked(isCheckedMeter);

        item = menu.findItem(R.id.garages);
        item.setChecked(isCheckedGarage);
        return true;
    }

    /**
     * Toolbar menu ItemSelected. Settings are stored in sharedprefs.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (item.getItemId()) {
            case R.id.garages:
                editor.putBoolean("garagebox", item.isChecked());
                break;
            case R.id.meters:
                editor.putBoolean("meterbox", item.isChecked());
                break;
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.maps) {
            showMap(mRouteObjects, mLocationObject);
        } else if (id == R.id.routes) {
            showRoutes(mRouteObjects);
        }
        // close drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
