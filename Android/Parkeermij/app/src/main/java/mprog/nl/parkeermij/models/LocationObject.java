package mprog.nl.parkeermij.models;

import java.io.Serializable;

/**
 * Created by Tamme on 2-6-2016.
 */
public class LocationObject implements Serializable {

    private double mLatitude;
    private double mLongitude;

    public LocationObject(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }


    public double getLongitude() {
        return mLongitude;
    }

}
