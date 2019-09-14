package org.matt.tweetsnearme.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("screen_name")
    public String name;

    @SerializedName("profile_image_url_https")
    public String profileImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
