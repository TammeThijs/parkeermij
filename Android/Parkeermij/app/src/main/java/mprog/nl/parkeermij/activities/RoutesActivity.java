package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        RouteAdapter.OnClickListener {

    public static final String TAG = "RoutesActivity";
    public static final String LOCATION = "location";
    public static final String ROUTES = "routes";

    private RouteAdapter mAdapter;

    @BindView(R.id.route_recycler)
    RecyclerView mRecyclerView;

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
        setContentView(R.layout.activity_routes);
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
}
