package org.matt.tweetsnearme.Repository;

import org.matt.tweetsnearme.Database.TweetDao;
import org.matt.tweetsnearme.Location.LocationService;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Network.TwitterService;

import java.util.List;

import io.reactivex.Single;


public class TweetRepository {

    private TweetDao tweetDao;
    private TwitterService twitterService;
    private LocationService locationService;

    public TweetRepository(TweetDao tweetDao, TwitterService twitterService, LocationService locationService) {
        this.tweetDao = tweetDao;
        this.twitterService = twitterService;
        this.locationService = locationService;
    }

    public Single<List<Tweet>> getTweets() {
        return twitterService.getTweets(locationService.getCurrentLocation(), 5, 10);
    }
}
