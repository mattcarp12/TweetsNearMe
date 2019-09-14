package org.matt.tweetsnearme.Model;

import java.util.List;

public class Coordinates {

    public List<Double> coordinates;

    public Coordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Double getLongitude() {
        return coordinates.get(0);
    }

    public Double getLatitude() {
        return coordinates.get(1);
    }
}
