package mprog.nl.parkeermij.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public static final String FRAGMENT_TAG = "routelistfragment";

    private RouteAdapter mAdapter;
    private List<RouteObject> mList;

    @BindView(R.id.recyclerview_wrapper)
    RecyclerView mRecyclerView;

    public RoutesListFragment() {
        // Required empty public constructor
    }

    /**
     * @param routes list of all parking locations nearby.
     * @return A new instance of fragment RoutesListFragment.
     */
    public static RoutesListFragment newInstance(List<RouteObject> routes) {

        RoutesListFragment fragment = new RoutesListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ROUTES, (Serializable) routes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle extras = getArguments();
             mList= (List<RouteObject>) extras.getSerializable(ROUTES);
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

    public void init(){

        mAdapter = new RouteAdapter(getActivity());
        mAdapter.setItems(mList, this ,new LocationObject(0,0));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void filterList(){
        mAdapter.filterRoutes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(LocationObject location, RouteObject routeObject, View view, String transition) {

    }
}
