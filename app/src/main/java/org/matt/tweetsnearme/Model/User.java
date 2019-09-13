package org.matt.tweetsnearme.Model;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {

    @SerializedName("name")
    private String name;

    @SerializedName("profile_image_url_https")
    private String profileImageUrlHttps;

    public String getName() {
        return name;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }
}
