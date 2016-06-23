package mprog.nl.parkeermij.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.adapters.RouteAdapter;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * TODO
 */
public class RoutesListFragment extends Fragment implements RouteAdapter.OnClickListener {

    private static final String ROUTES = "routes";
    public static final String START_LOCATION = "startlocation";
    public static final String TAG = "routelistfragment";
    public static final String BASE_URL = "http://maps.google.com/maps?";


    private RouteAdapter mAdapter;
    private List<RouteObject> mList;
    private LocationObject mLocationObject;

    @BindView(R.id.recyclerview_wrapper)
    RecyclerView mRecyclerView;

    public RoutesListFragment() {
        // Required empty public constructor
    }

    /**
     * @param routes list of all parking locations nearby.
     * @return A new instance of fragment RoutesListFragment.
     */
    public static RoutesListFragment newInstance(List<RouteObject> routes,
                                                 LocationObject locationObject) {

        RoutesListFragment fragment = new RoutesListFragment();
        Bundle extras = new Bundle();
        extras.putSerializable(ROUTES, (Serializable) routes);
        extras.putSerializable(START_LOCATION, locationObject);
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle extras = getArguments();
            mList = (List<RouteObject>) extras.getSerializable(ROUTES);
            mLocationObject = (LocationObject) extras.getSerializable(START_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes_list, container, false);
        ButterKnife.bind(this, view);
        init();

        return view;
    }

    public void init() {

        mAdapter = new RouteAdapter(getActivity());
        mAdapter.setItems(mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void filterList() {
        mAdapter.filterRoutes();
    }

    @Override
    public void onClick(RouteObject routeObject) {
        Log.d("clicked", "onClick: CLICKED" + routeObject.getCost());
        startRouteMaps(routeObject);
    }

    public void startRouteMaps(RouteObject routeObject) {

        String saddr = "saddr=" + String.valueOf(mLocationObject.getLatitude())
                + "," + String.valueOf(mLocationObject.getLongitude());
        String daddr = "&daddr=" + routeObject.getLatitude() + "," + routeObject.getLongitude();
        String URL = BASE_URL + saddr + daddr;
        Log.d(TAG, "startRouteMaps: " + URL);


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(URL));
        startActivity(intent);
    }
}

