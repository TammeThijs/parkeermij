package mprog.nl.parkeermij.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Tamme on 22-6-2016.
 * model that extends cluster item so it can be used by Google Maps Utisl
 */
public class ClusterObject implements ClusterItem {
    private final LatLng mPosition;
    private boolean isGarage = false;

    public ClusterObject(LatLng position) {
        mPosition = position;
    }

    public ClusterObject(Double lat, Double lon){
        mPosition = new LatLng(lat, lon);
        isGarage = true;
    }

    public boolean getType(){
        return isGarage;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
