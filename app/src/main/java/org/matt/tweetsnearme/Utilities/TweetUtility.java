package org.matt.tweetsnearme.Utilities;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.matt.tweetsnearme.R;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

// TODO: Rewrite this class so that it uses Retrofit API instead of Twitter4j API
public class TweetUtility {

    private static final String TAG = TweetUtility.class.getSimpleName();
    private Context mContext;
    private ConfigurationBuilder cb;
    private TwitterFactory tf;
    private Twitter twit;
    private Query mQuery;
    private QueryResult mQueryResult;
    private static final int TWEET_RADIUS = 1;
    private static final int MAX_TWEETS = 50;


    public TweetUtility(Context mContext) {
        this.mContext = mContext;
        configure();
    }

    private void configure() {
        cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(mContext.getResources().getString(R.string.twitter_api_consumer_key))
                .setOAuthConsumerSecret(mContext.getResources().getString(R.string.twitter_api_consumer_secret_key))
                .setOAuthAccessToken(mContext.getResources().getString(R.string.twitter_api_access_token))
                .setOAuthAccessTokenSecret(mContext.getResources().getString(R.string.twitter_api_access_token_secret));

        tf = new TwitterFactory(cb.build());
        twit = tf.getInstance();
        Log.d(TAG, "twitter configuration complete");
    }

    public List tweetQuery(LatLng mLatLng) {
        mQuery = new Query()
                .count(MAX_TWEETS)
                .sinceId(1)
                .geoCode(new GeoLocation(mLatLng.latitude, mLatLng.longitude), TWEET_RADIUS, Query.MILES);
        Log.d(TAG, "sinceId: " + mQuery.getSince());
        try {
            mQueryResult = twit.search(mQuery);
            Log.d(TAG, "Querying twitter");
            List<Status> statusList = mQueryResult.getTweets();
            Log.d(TAG, "number of tweets: " + statusList.size());
            return statusList;


        } catch (TwitterException e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
        return null;
    }





}
