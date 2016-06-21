package mprog.nl.parkeermij.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tamme on 21-6-2016.
 */
public class CoordinatesWrapper implements Serializable {

    @SerializedName("coordinates")
    private String[] mCoordinates;

    public String[] getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(String[] coordinates) {
        mCoordinates = coordinates;
    }
}
