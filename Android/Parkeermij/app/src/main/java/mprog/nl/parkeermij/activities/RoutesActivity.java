package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.MVP.presenters.RoutesActivityPresenter;
import mprog.nl.parkeermij.MVP.views.RoutesActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.adapters.RouteAdapter;
import mprog.nl.parkeermij.dagger.components.DaggerRoutesActivityComponent;
import mprog.nl.parkeermij.dagger.modules.RoutesActivityModule;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

public class RoutesActivity extends AppCompatActivity implements RoutesActivityView,
        RouteAdapter.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "RoutesActivity";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";

    private RouteAdapter mAdapter;

    @BindView(R.id.route_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    RoutesActivityPresenter mPresenter;

    public static Intent newIntent(Context context, Location location, List<RouteObject> routeObjects) {
        Intent intent = new Intent(context, RoutesActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAdapter = new RouteAdapter(getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.init(getIntent());
    }

    @Override
    public void setRecycerData(List<RouteObject> routeList, LocationObject locationObject) {
        mAdapter.setItems(routeList, this, locationObject);
    }

    @Override
    public void startMap(LocationObject location, RouteObject routeObject, View view, String transition) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                view, transition);

        Intent intent = MapsActivity.newIntent(this, location, routeObject, transition);
        startActivity(intent, options.toBundle());
    }


    @Override
    public void onClick(LocationObject location, RouteObject routeObject, View view,
                        String transition) {
        mPresenter.startMap(location, routeObject, view, transition);
    }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
