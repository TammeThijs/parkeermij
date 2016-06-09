package mprog.nl.parkeermij.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MainViewHolder> {

    private List<RouteObject> mRoutes;
    private Context mContext;

    public RouteAdapter(Context context) {
        mContext = context;
    }

    public void setItems(List<RouteObject> routes) {
        mRoutes = routes;
    }

    public interface onClick {
        void onClick(int position, View view);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        final MainViewHolder viewHolder = holder;

        viewHolder.mMapView.onCreate(null);
        viewHolder.mMapView.getMapAsync(new MapReadyCallback());

        viewHolder.mCosts.setText(new StringBuilder().append("€").append(mRoutes.get(position)
                .getCost()).toString());
        viewHolder.mDistance.setText(new StringBuilder().append(mRoutes.get(position)
                .getDist()).append("m").toString());
        viewHolder.mTypeVal.setText(mRoutes.get(position).getType());
    }

    @Override
    public void onViewRecycled(MainViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public RouteObject getItem(int id){
        return mRoutes.get(id);
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    private class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            GoogleMap mMap = googleMap;
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(52.355526, 4.953911), 15));
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.map_view)
        MapView mMapView;

        @BindView(R.id.distance_value)
        TextView mDistance;

        @BindView(R.id.costs_value)
        TextView mCosts;

        @BindView(R.id.type)
        TextView mType;

        @BindView(R.id.type_value)
        TextView mTypeVal;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
