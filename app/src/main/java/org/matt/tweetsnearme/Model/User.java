package org.matt.tweetsnearme.Model;

import com.google.gson.annotations.SerializedName;

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
