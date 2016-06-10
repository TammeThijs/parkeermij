package mprog.nl.parkeermij.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import mprog.nl.parkeermij.models.LocationObject;
import mprog.nl.parkeermij.models.RouteObject;

/**
 * Created by Tamme on 8-6-2016.
 */
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MainViewHolder>{

    public static final String TAG = "RouteAdapter";
    private List<RouteObject> mRoutes;
    private Context mContext;
    private LocationObject mLocationObject;
    private String URL_MAPS_STATIC = "";
    private OnClickListener mOnClickListener;

    public RouteAdapter(Context context) {
        mContext = context;
        URL_MAPS_STATIC = mContext.getString(R.string.maps_static_url);
    }

    public void setItems(List<RouteObject> routes, OnClickListener listener,
                         LocationObject locationObject) {
        mRoutes = routes;
        mOnClickListener = listener;
        mLocationObject = locationObject;
    }

    public interface OnClickListener {
        void onClick(LocationObject location, RouteObject routeObject , View view, String transition);
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

        holder.mMapView.setOnClickListener(new View.OnClickListener() {
            
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(mLocationObject, mRoute, v ,v.getTransitionName());
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
}
