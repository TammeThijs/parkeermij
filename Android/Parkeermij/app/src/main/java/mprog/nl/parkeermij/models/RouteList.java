package mprog.nl.parkeermij.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tamme on 7-6-2016.
 *
 * wrapper for RouteObject object *
 */
public class RouteList {

    @SerializedName("reccommendations")
    private List<RouteObject> mRouteObjectList;

    public RouteList(List<RouteObject> routeObjectList) {
        mRouteObjectList = routeObjectList;
    }

    public List<RouteObject> getRouteObjectList() {
        return mRouteObjectList;
    }

    public void setRouteObjectList(List<RouteObject> mRouteObjectList) {
        this.mRouteObjectList = mRouteObjectList;
    }
}
