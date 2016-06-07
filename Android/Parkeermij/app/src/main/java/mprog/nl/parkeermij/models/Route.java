package mprog.nl.parkeermij.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tamme on 7-6-2016.
 */
public class Route implements Serializable {

    @SerializedName("dist_in_meters")
    private String mDist;

    @SerializedName("cost")
    private String mCost;

    @SerializedName("name")
    private String mName;

    @Nullable
    @SerializedName("automat_number")
    private String mAutomatNum;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("lat")
    private String mLatitude;

    @SerializedName("lon")
    private String mLongitude;

    @SerializedName("type")
    private String mType;

    @SerializedName("garageid")
    private String mGarageId;

    @SerializedName("garage_infourl")
    private String mGarageUrl;

    public String getDist() {
        return mDist;
    }

    public void setDist(String dist) {
        mDist = dist;
    }

    public String getCost() {
        return mCost;
    }

    public void setCost(String cost) {
        mCost = cost;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Nullable
    public String getAutomatNum() {
        return mAutomatNum;
    }

    public void setAutomatNum(@Nullable String automatNum) {
        mAutomatNum = automatNum;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getGarageId() {
        return mGarageId;
    }

    public void setGarageId(String garageId) {
        mGarageId = garageId;
    }

    public String getGarageUrl() {
        return mGarageUrl;
    }

    public void setGarageUrl(String garageUrl) {
        mGarageUrl = garageUrl;
    }
}
