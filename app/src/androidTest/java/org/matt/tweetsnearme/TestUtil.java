package org.matt.tweetsnearme;

import org.matt.tweetsnearme.Model.Coordinates;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TestUtil {

    public static List<Tweet> getTweetList() {
        Tweet tweet1 = getTweet1();
        Tweet tweet2 = getTweet2();
        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);
        return tweetList;
    }

    private static Tweet getTweet1() {
        Tweet tweet1 = new Tweet();
        tweet1.setId(1);
        tweet1.setText("Foo");
        List<Double> coords = new ArrayList<>(Arrays.asList(40.190, -81.45));
        tweet1.setCoordinates(new Coordinates(coords));
        tweet1.setUser(new User());
        tweet1.getUser().setName("Matt");
        tweet1.getUser().setProfileImageUrl("https:\\/\\/pbs.twimg.com\\/profile_images\\/942244445705027584\\/EzelBeW1_normal.jpg");
        return tweet1;
    }

    private static Tweet getTweet2() {
        Tweet tweet2 = new Tweet();
        tweet2.setId(1);
        tweet2.setText("Foo");
        List<Double> coords = new ArrayList<>(Arrays.asList(41.190, -82.45));
        tweet2.setCoordinates(new Coordinates(coords));
        tweet2.setUser(new User());
        tweet2.getUser().setName("Matt");
        tweet2.getUser().setProfileImageUrl("https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png");
        return tweet2;
    }
}
