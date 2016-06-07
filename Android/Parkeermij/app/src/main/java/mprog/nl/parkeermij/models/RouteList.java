package mprog.nl.parkeermij.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tamme on 7-6-2016.
 *
 * wrapper for Route object *
 */
public class RouteList {

    @SerializedName("reccommendations")
    private List<Route> mRouteList;

    public RouteList(List<Route> routeList) {
        mRouteList = routeList;
    }

    public List<Route> getRouteList() {
        return mRouteList;
    }

    public void setRouteList(List<Route> mRouteList) {
        this.mRouteList = mRouteList;
    }
}
