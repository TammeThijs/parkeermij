package mprog.nl.parkeermij.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tamme on 20-6-2016.
 */
public class MeterObject implements Serializable {

    @SerializedName("sellingpointdesc")
    private String mSellingPontDesc;

    @SerializedName("areamanagerid")
    private String mAreaManagerId;

    @SerializedName("sellingpointid")
    private String mSellingPointId;

    @SerializedName("location")
    private CoordinatesWrapper mCoordinates;

    public String getSellingPontDesc() {
        return mSellingPontDesc;
    }

    public void setSellingPontDesc(String sellingPontDesc) {
        mSellingPontDesc = sellingPontDesc;
    }

    public String getAreaManagerId() {
        return mAreaManagerId;
    }

    public void setAreaManagerId(String areaManagerId) {
        mAreaManagerId = areaManagerId;
    }

    public String getSellingPointId() {
        return mSellingPointId;
    }

    public void setSellingPointId(String sellingPointId) {
        mSellingPointId = sellingPointId;
    }
}

