package mprog.nl.parkeermij.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.helpers.CustomRenderer;
import mprog.nl.parkeermij.helpers.PolygonHelper;
import mprog.nl.parkeermij.models.ClusterObject;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener {

    public static final String LOCATION = "location";
    public static final String ROUTE = "routes";
    public static final String METERS = "meters";
    public static final float MAX_ZOOM = 12.0f;

    // Parking Zones
    public static final int ZONE1 = 0;
    public static final int ZONE2 = 1;
    public static final int ZONE3 = 2;
    public static final int ZONE4 = 3;
    public static final int ZONE5 = 4;
    public static final int ZONE6 = 5;
    public static final int ZONE7 = 6;

    @BindView(R.id.content)
    LinearLayout mContent;

    private GoogleMap mMap;
    private PolygonHelper mPolygonHelper;
    private List<List<List<LatLng>>> mAreaList;
    private SupportMapFragment mSupportMapFragment;
    private LocationObject mStartLocation;
    private List<RouteObject> mParkLocations;
    private List<LatLng> mMeterLocations;
    private ClusterManager mClusterManager;
    private boolean isAreaSet = false;

    public MapsFragment() {
        // required empty constructor
    }

    public static MapsFragment newInstance(LocationObject location,
                                           List<RouteObject> routeObject,
                                           List<LatLng> meters) {
        MapsFragment fragment = new MapsFragment();
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
        extras.putSerializable(ROUTE, (Serializable) routeObject);
        extras.putSerializable(METERS, (Serializable) meters);
        fragment.setArguments(extras);

        return fragment;
    }

    /**
     * retrieve data before creating view
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPolygonHelper = new PolygonHelper(getActivity());
        // retrieve bundle
        if (getArguments() != null) {
            Bundle extras = getArguments();
            mStartLocation = (LocationObject) extras.getSerializable(LOCATION);
            mParkLocations = (List<RouteObject>) extras.getSerializable(ROUTE);
            mMeterLocations = (List<LatLng>) extras.getSerializable(METERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        // create map
        if (mSupportMapFragment == null) {
            mSupportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map));
            mSupportMapFragment.getMapAsync(this);
        }

        return view;
    }

    /**
     * Setup GoogleMap
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // set maps to current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mStartLocation.getLatitude(),
                mStartLocation.getLongitude()), 14));

        //Initialize Google Maps Utils
        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mClusterManager.setRenderer(new CustomRenderer(getActivity(), mMap, mClusterManager));

        // Map settings
        mMap.setOnCameraChangeListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        mMap.setOnCameraChangeListener(mClusterManager);

        setMarkers();
    }

    public void setMarkers() {
        // clear markers
        mClusterManager.clearItems();

        // load settings
        SharedPreferences settings = getActivity().getSharedPreferences(getActivity()
                .getString(R.string.shared_settings), 0);
        boolean isCheckedMeter = settings.getBoolean(getActivity().getString(R.string.show_meters), false);
        boolean isCheckedGarage = settings.getBoolean(getActivity().getString(R.string.show_garages), false);

        // add markers depending on settings
        if (isCheckedGarage) {
            for (RouteObject route : mParkLocations) {
                Double lat = Double.parseDouble(route.getLatitude());
                Double lon = Double.parseDouble(route.getLongitude());

                mClusterManager.addItem(new ClusterObject(lat, lon));
            }
        }
        if (isCheckedMeter) {
            for (LatLng meterCoordinates : mMeterLocations) {
                // already Latlng object
                mClusterManager.addItem(new ClusterObject(meterCoordinates));
            }

        }
        // set clustered markers
        mClusterManager.cluster();

        // Set areas if needed
        if (!isAreaSet) {
            setAreas();
        }
    }

    /**
     * Paints polygon lines to display parking areas within amsterdam
     *
     */
    public void setAreas() {

        int normal = 0;
        int transparent = 0;
        mAreaList = mPolygonHelper.parseXML();

        for (int zone = 0; zone < mAreaList.size(); zone++) {

            // Pick colors
            if (zone == ZONE1) {
                normal = ContextCompat.getColor(getContext(), R.color.zone1);
                transparent = ContextCompat.getColor(getContext(), R.color.zone1_transparent);
            } else if (zone == ZONE2) {
                normal = ContextCompat.getColor(getContext(), R.color.zone2);
                transparent = ContextCompat.getColor(getContext(), R.color.zone2_transparent);
            } else if (zone == ZONE3) {
                normal = ContextCompat.getColor(getContext(), R.color.zone3);
                transparent = ContextCompat.getColor(getContext(), R.color.zone3_transparent);
            } else if (zone == ZONE4) {
                normal = ContextCompat.getColor(getContext(), R.color.zone4);
                transparent = ContextCompat.getColor(getContext(), R.color.zone4_transparent);
            } else if (zone == ZONE5) {
                normal = ContextCompat.getColor(getContext(), R.color.zone5);
                transparent = ContextCompat.getColor(getContext(), R.color.zone5_transparent);
            } else if (zone == ZONE6) {
                normal = ContextCompat.getColor(getContext(), R.color.zone6);
                transparent = ContextCompat.getColor(getContext(), R.color.zone6_transparent);
            } else if (zone == ZONE7) {
                normal = ContextCompat.getColor(getContext(), R.color.zone7);
                transparent = ContextCompat.getColor(getContext(), R.color.zone7_transparent);
            }

            // paint zones
            for (int subzone = 0; subzone < mAreaList.get(zone).size(); subzone++) {

                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .addAll(mAreaList.get(zone).get(subzone))
                        .strokeWidth(3)
                        .strokeColor(normal)
                        .fillColor(transparent));
            }
        }
        // mark area is painted
        isAreaSet = true;
    }

    /**
     * Avoids user zooming out too far
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        float maxZoom = MAX_ZOOM;
        if (cameraPosition.zoom < maxZoom) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
            mMap.getUiSettings().setZoomGesturesEnabled(false); // dissable gestures
        }
        if (cameraPosition.zoom < maxZoom) {
            mMap.getUiSettings().setZoomGesturesEnabled(true); // re-enable gestures
        }
    }
}
