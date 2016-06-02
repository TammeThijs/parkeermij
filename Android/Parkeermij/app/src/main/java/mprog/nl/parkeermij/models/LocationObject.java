package mprog.nl.parkeermij.models;

import java.io.Serializable;

/**
 * Created by Prins Tamme on 2-6-2016.
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

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
