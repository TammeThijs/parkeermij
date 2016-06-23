package mprog.nl.parkeermij.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MainViewHolder>{

    public static final String TAG = "RouteAdapter";
    private List<RouteObject> mRoutes;
    private List<RouteObject> mRoutesAll;
    private Context mContext;
    private LocationObject mLocationObject;
    private String URL_MAPS_STATIC = "";
    private OnClickListener mOnClickListener;

    public RouteAdapter(Context context) {
        mContext = context;
        URL_MAPS_STATIC = mContext.getString(R.string.maps_static_url);
    }

    public void setItems(List<RouteObject> routes, OnClickListener listener) {
        mRoutesAll = routes;
        mOnClickListener = listener;
        filterRoutes();
    }

    public void filterRoutes(){
        mRoutes = new ArrayList<>();

        // load settings
        SharedPreferences settings = mContext.getSharedPreferences(mContext.getString(R.string.shared_settings), 0);
        boolean isDistanceSort = settings.getBoolean(mContext.getString(R.string.sort_distance), false);

        // filter items
        for (RouteObject route: mRoutesAll) {
                mRoutes.add(route);
        }

        // select comperator based on settings
        CostComperator costComperator = new CostComperator();
        DistComperator distComperator = new DistComperator();

        if(isDistanceSort){
            Collections.sort(mRoutes, distComperator);
        } else {
            Collections.sort(mRoutes, costComperator);
        }

        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(RouteObject routeObject);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        final MainViewHolder viewHolder = holder;
        final RouteObject mRoute = getItem(position);
        String mSringLocation;

        viewHolder.mCosts.setText(new StringBuilder().append("€").append(mRoutes.get(position)
                .getCost()).toString());
        viewHolder.mDistance.setText(new StringBuilder().append(mRoutes.get(position)
                .getDistString()).toString());
        viewHolder.mTypeVal.setText(mRoutes.get(position).getType());

        // setup string for static maps
        mSringLocation = getItem(position).getLatitude() + "," + getItem(position).getLongitude();
        String URL = URL_MAPS_STATIC + mSringLocation + "&markers=|" + mSringLocation + "|";
        
        Picasso.with(holder.mMapView.getContext())
                .load(URL)
                .fit()
                .centerCrop()
                .into(holder.mMapView);

        holder.mMapView.setOnClickListener(new View.OnClickListener() {
            
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(mRoute);
            }
        });
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

    public class CostComperator implements Comparator<RouteObject> {

        @Override
        public int compare(RouteObject route1, RouteObject route2) {
            Double cost1 = Double.parseDouble(route1.getCost());
            Double cost2 = Double.parseDouble(route2.getCost());

            return cost1.compareTo(cost2);
        }
    }

    public class DistComperator implements Comparator<RouteObject> {

        @Override
        public int compare(RouteObject route1, RouteObject route2) {
            Double dist1 = Double.parseDouble(route1.getDist());
            Double dist2 = Double.parseDouble(route2.getDist());

            return dist1.compareTo(dist2);
        }
    }


}
