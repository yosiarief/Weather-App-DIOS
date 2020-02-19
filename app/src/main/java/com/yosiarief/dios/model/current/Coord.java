
package com.yosiarief.dios.model.current;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Coord {

    @SerializedName("lat")
    private float mLat;
    @SerializedName("lon")
    private Long mLon;

    public float getLat() {
        return mLat;
    }

    public void setLat(float lat) {
        mLat = lat;
    }

    public Long getLon() {
        return mLon;
    }

    public void setLon(Long lon) {
        mLon = lon;
    }

}
