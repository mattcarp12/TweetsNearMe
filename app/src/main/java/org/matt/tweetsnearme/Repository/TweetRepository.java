package org.matt.tweetsnearme.Repository;

import android.app.Application;
import android.location.Location;

import org.matt.tweetsnearme.Database.TweetDao;
import org.matt.tweetsnearme.Database.TweetDatabase;
import org.matt.tweetsnearme.Location.LocationService;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Network.TwitterService;

import java.util.List;

import io.reactivex.Single;


public class TweetRepository {

    private TweetDao tweetDao;
    private TwitterService twitterService;
    private LocationService locationService;

    public TweetRepository(Application application) {
        this.tweetDao = TweetDatabase.getDatabase(application).tweetDao();
        this.twitterService = new TwitterService();
        this.locationService = new LocationService(application);
    }

    public Single<List<Tweet>> getTweets() {
        return twitterService.getTweets(locationService.getCurrentLocation(), 5, 10);
    }

    public Location getLocation() {
        return locationService.getCurrentLocation();
    }
}
