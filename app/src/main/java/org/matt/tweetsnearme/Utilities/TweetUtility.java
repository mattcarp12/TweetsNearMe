package org.matt.tweetsnearme.Utilities;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.matt.tweetsnearme.MapsActivity;
import org.matt.tweetsnearme.R;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TweetUtility {

    private static final String TAG = TweetUtility.class.getSimpleName();
    private Context mContext;
    private ConfigurationBuilder cb;
    private TwitterFactory tf;
    private Twitter twit;
    private Query mQuery;
    private QueryResult mQueryResult;
    private static final int TWEET_RADIUS = 1;


    public TweetUtility(Context mContext) {
        this.mContext = mContext;
        configure();
    }

    private void configure() {
        cb = new ConfigurationBuilder();
        String twitter_api_key = mContext.getResources().getString(R.string.twitter_api_consumer_key);
        String twitter_api_secret_key = mContext.getResources().getString(R.string.twitter_api_consumer_secret_key);
        String twitter_api_access_token = mContext.getResources().getString(R.string.twitter_api_access_token);
        String twitter_api_access_token_secret = mContext.getResources().getString(R.string.twitter_api_access_token_secret);
        Log.d(TAG, "Twitter API key: " + twitter_api_key);
        Log.d(TAG, "Twitter API secret key: " + twitter_api_secret_key);
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(twitter_api_key)
          .setOAuthConsumerSecret(twitter_api_secret_key)
          .setOAuthAccessToken(twitter_api_access_token)
          .setOAuthAccessTokenSecret(twitter_api_access_token_secret);
        tf = new TwitterFactory(cb.build());
        twit = tf.getInstance();
        Log.d(TAG, "twitter configuration complete");
    }

    public String tweetQuery(LatLng mLatLng) {
        mQuery = new Query().geoCode(new GeoLocation(mLatLng.latitude, mLatLng.longitude), TWEET_RADIUS, Query.MILES);
        try {
            mQueryResult = twit.search(mQuery);
            Log.d(TAG, "Querying twitter");
            List<Status> statusList = mQueryResult.getTweets();
            Log.d(TAG, "number of tweets: " + statusList.size());
            if (statusList.size() != 0) {
                return statusList.get(0).getText();
            } else return "No tweets nearby.";


        } catch (TwitterException e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
        return null;
    }





}
