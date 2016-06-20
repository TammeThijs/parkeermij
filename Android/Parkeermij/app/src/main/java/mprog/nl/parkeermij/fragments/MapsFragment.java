package mprog.nl.parkeermij.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.helpers.PolygonHelper;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener {

    public static final String TAG = "MapActivity";
    public static final String LOCATION = "location";
    public static final String ROUTE = "routes";
    public static final String FRAGMENT_TAG = "MAPS_FRAGMENT";
    public static final float MAX_ZOOM = 12.0f;

    @BindView(R.id.content)
    LinearLayout mContent;

    private GoogleMap mMap;
    private PolygonHelper mPolygonHelper;
    private List<List<LatLng>> mAreaList;
    private SupportMapFragment mSupportMapFragment;
    private LocationObject startLocation;
    private List<RouteObject> parkLocations;

    public MapsFragment() {
        // required empty constructor
    }

    public static MapsFragment newInstance(Context context, LocationObject location, List<RouteObject> routeObject) {
        MapsFragment fragment = new MapsFragment();
        Bundle extras = new Bundle();

        extras.putSerializable(LOCATION, location);
        extras.putSerializable(ROUTE, (Serializable) routeObject);
        fragment.setArguments(extras);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPolygonHelper = new PolygonHelper(getActivity());

        if (getArguments() != null) {
            Bundle extras = getArguments();
            startLocation = (LocationObject) extras.getSerializable(LOCATION);
            parkLocations = (List<RouteObject>) extras.getSerializable(ROUTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        if (mSupportMapFragment == null) {
            mSupportMapFragment = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map));
            mSupportMapFragment.getMapAsync(this);
        }

        mAreaList = mPolygonHelper.parseXML();
        Log.d(TAG, "onCreateView: SIZE LIST: "+mAreaList.size());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraChangeListener(this);
        mMap.setMyLocationEnabled(true);

        setMarkers();        
        // set maps to current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLocation.getLatitude(),
                startLocation.getLongitude()), 14));
    }

    public void setMarkers() {
        // clear markers
        mMap.clear();

        // load settings
        SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
        boolean isCheckedMeter = settings.getBoolean("meterbox", false);
        boolean isCheckedGarage = settings.getBoolean("garagebox", false);

        // custom markers
        BitmapDescriptor meter = BitmapDescriptorFactory.fromResource(R.drawable.parkmeter);
        BitmapDescriptor garage = BitmapDescriptorFactory.fromResource(R.drawable.garage);

        for (RouteObject route : parkLocations) {

            boolean isChecked = route.getType().equals("garage") ? isCheckedGarage : isCheckedMeter;

            if (isChecked) {
                Double lat = Double.parseDouble(route.getLatitude());
                Double lon = Double.parseDouble(route.getLongitude());

                BitmapDescriptor icon = route.getType().equals("garage") ? garage : meter;
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                        lon)).icon(icon).title(route.getName()).snippet(route.getAddress()));
            }
        }

        setAreas();
    }

    public void setAreas(){

        int normal=0;
        int transparent=0;


        for (int i = 0; i < mAreaList.size() ; i++) {
            if(i < 2){
                normal = ContextCompat.getColor(getContext(), R.color.zone1);
                transparent = ContextCompat.getColor(getContext(), R.color.zone1Transparent);
            } else if(i < 7){
                normal = ContextCompat.getColor(getContext(), R.color.zone2);
                transparent = ContextCompat.getColor(getContext(), R.color.zone2Transparent);
            } else if(i < 16){
                normal = ContextCompat.getColor(getContext(), R.color.zone3);
                transparent = ContextCompat.getColor(getContext(), R.color.zone3Transparent);
            } else if(i < 31){
                normal = ContextCompat.getColor(getContext(), R.color.zone4);
                transparent = ContextCompat.getColor(getContext(), R.color.zone4Transparent);
            }else if(i < 50){
                normal = ContextCompat.getColor(getContext(), R.color.zone5);
                transparent = ContextCompat.getColor(getContext(), R.color.zone5Transparent);
            }


            Polygon polygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(mAreaList.get(i))
                    .strokeWidth(3)
                    .strokeColor(normal)
                    .fillColor(transparent));
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: CALLED");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: CALLED");
    }

    /**
     * Avoids user zooming out too far
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