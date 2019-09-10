package org.matt.tweetsnearme.Model;

import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("text")
    private String text;

    @SerializedName("user")
    private User user;

    @SerializedName("coordinates")
    private Coordinates coordinates;

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
