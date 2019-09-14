package org.matt.tweetsnearme.Model;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Double> stringToCoordinateList(String coordinateString) {
        List<Double> coordinates = new ArrayList<>();
        for (String s : coordinateString.split(","))
            coordinates.add(Double.parseDouble(s));
        return coordinates;
    }


    @TypeConverter
    public static String coordinatesToString(List<Double> coordinates) {
        return coordinates.get(0) + "," + coordinates.get(1);
    }
}
