package mprog.nl.parkeermij.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamme on 20-6-2016.
 */
public class MeterObject {

    @SerializedName("sellingpointdesc")
    private String mDistance;

    @SerializedName("areamanagerid")
    private String mAreaManagerId;

    @SerializedName("sellingpointid")
    private String mSellingPointId;

    @SerializedName("location")
    private CoordinatesWrapper mCoordinates;

}

