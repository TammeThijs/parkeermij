package mprog.nl.parkeermij.helpers;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import mprog.nl.parkeermij.models.ClusterObject;

/**
 * Created by Tamme on 22-6-2016.
 *
 * Custom DefaultClusterRenderer which is required when using maps utils. Overriding
 * methods to customize markers.
 *
 */
public class CustomRenderer extends DefaultClusterRenderer<ClusterObject> {
    public CustomRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
    }


    @Override
    protected void onBeforeClusterItemRendered(ClusterObject item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        if (item != null) {
            float color = item.getType() ? BitmapDescriptorFactory.HUE_GREEN : BitmapDescriptorFactory.HUE_AZURE;
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
        }
    }
}
