package org.matt.tweetsnearme.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tweets")
public class Tweet {

    @PrimaryKey(autoGenerate = true)
    private int id;

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
