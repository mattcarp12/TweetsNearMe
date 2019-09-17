package org.matt.tweetsnearme.Network;


import android.location.Location;

import org.matt.tweetsnearme.Model.OAuthToken;
import org.matt.tweetsnearme.Model.Tweet;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterService {

    private static final String TAG = TwitterService.class.getSimpleName();
    private static final String TWITTER_API_CONSUMER_KEY = "ko9A8JKUa1HFALBY1LwLYICWq";
    private static final String TWITTER_API_CONSUMER_SECRET_KEY = "k8bVfThsns8392bvdbFF2Ule2dFEKfMO7PjEwOfE1bWgleEnNI";
    private static final String credentials = Credentials.basic(TWITTER_API_CONSUMER_KEY, TWITTER_API_CONSUMER_SECRET_KEY);
    private static OAuthToken token;

    // TODO : Make methods to create OkHttpClient and TwitterApi

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                    token != null ? token.getAuthorization() : credentials);
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private static TwitterApi twitterApi = new Retrofit.Builder()
            .baseUrl(TwitterApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TwitterApi.class);

    private Single<OAuthToken> getToken() {
        // TODO : Refresh token if no longer valid
        if (token != null) return Single.just(token);
        else return twitterApi.postCredentials("client_credentials");
    }

    public Single<List<Tweet>> getTweets(Single<Location> currLoc, int radius, int maxTweets) {
        return getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(oAuthToken -> {
                    token = oAuthToken;
                    return currLoc;
                })
                .flatMap(location -> {
                    String geoCodeString = location.getLatitude() + "," +
                            location.getLongitude() + "," +
                            radius + "mi";
                    return twitterApi.getTweets(geoCodeString, maxTweets, 1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(search -> search.getTweets());
                });
    }

}
