package org.matt.tweetsnearme.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coordinates {

    public static final int INDEX_LONGITUDE = 0;
    public static final int INDEX_LATITUDE = 1;

    @SerializedName("coordinates")
    public List<Double> coordinates;

    public Double getLongitude() {
        return coordinates.get(INDEX_LONGITUDE);
    }

    public Double getLatitude() {
        return coordinates.get(INDEX_LATITUDE);
    }
}
