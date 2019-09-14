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
    // TODO : Make custom deserializer to flatten JSON

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

    public void getToken() {
        twitterApi.postCredentials("client_credentials")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(emittedData -> token = emittedData,
                        error -> System.out.println("Error occured: " + error));
    }

    public Single<List<Tweet>> getTweets(Location currLoc, int radius, int maxTweets) {
        String geoCodeString = currLoc.getLatitude() + "," +
                currLoc.getLongitude() + "," +
                radius + "mi";
        return twitterApi.getTweets(geoCodeString, maxTweets, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(search -> search.getTweets());
    }

}
