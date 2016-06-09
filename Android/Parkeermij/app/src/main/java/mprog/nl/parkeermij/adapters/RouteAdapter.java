package mprog.nl.parkeermij.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MainViewHolder>{

    public static final String TAG = "RouteAdapter";
    private List<RouteObject> mRoutes;
    private Context mContext;
    private final String URL_MAPS_STATIC = "https://maps.googleapis.com/maps/api/staticmap?zoom="+
            "15&size=400x400&center=";

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
        String mSringLocation;

        viewHolder.mCosts.setText(new StringBuilder().append("€").append(mRoutes.get(position)
                .getCost()).toString());
        viewHolder.mDistance.setText(new StringBuilder().append(mRoutes.get(position)
                .getDist()).append("m").toString());
        viewHolder.mTypeVal.setText(mRoutes.get(position).getType());

        // setup string for static maps
        mSringLocation = getItem(position).getLatitude() + "," + getItem(position).getLongitude();
        String URL = URL_MAPS_STATIC + mSringLocation + "&markers=|" + mSringLocation + "|";
        
        Picasso.with(holder.mMapView.getContext())
                .load(URL)
                .fit()
                .centerCrop()
                .into(holder.mMapView);
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


    public class MainViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.map_view)
        ImageView mMapView;

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
