package org.matt.tweetsnearme.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("statuses")
    private List<Tweet> tweets;

    public List<Tweet> getTweets() {
        return tweets;
    }
}