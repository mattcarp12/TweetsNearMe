package org.matt.tweetsnearme.Network;

import android.location.Location;

import org.matt.tweetsnearme.Model.OAuthToken;
import org.matt.tweetsnearme.Model.Tweet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;

public class TwitterService {

    private static final String TAG = TwitterService.class.getSimpleName();
    private static final String TWITTER_API_CONSUMER_KEY = "ko9A8JKUa1HFALBY1LwLYICWq";
    private static final String TWITTER_API_CONSUMER_SECRET_KEY = "k8bVfThsns8392bvdbFF2Ule2dFEKfMO7PjEwOfE1bWgleEnNI";
    private static final String credentials = Credentials.basic(TWITTER_API_CONSUMER_KEY, TWITTER_API_CONSUMER_SECRET_KEY);
    private static OAuthToken token;

    private final TwitterApi twitterApi;

    @Inject
    public TwitterService(TwitterApi twitterApi) {
        this.twitterApi = twitterApi;

    }

    private Observable<OAuthToken> getToken() {
        // TODO : Refresh token if no longer valid
        if (token != null) return Observable.just(token);
        else return twitterApi.postCredentials("client_credentials");
    }

    public Observable<List<Tweet>> getTweets(Single<Location> currLoc, int radius, int maxTweets) {
        return getToken()
                .subscribeOn(Schedulers.io())
                .flatMap(oAuthToken -> {
                    token = oAuthToken;
                    return currLoc.toObservable();
                })
                .flatMap(location -> {
                    String geoCodeString = location.getLatitude() + "," +
                            location.getLongitude() + "," +
                            radius + "mi";
                    return twitterApi.getTweets(geoCodeString, maxTweets, 1, "recent")
                            .map(search -> search.getTweets())
                            .subscribeOn(Schedulers.io());
                });
    }

}
