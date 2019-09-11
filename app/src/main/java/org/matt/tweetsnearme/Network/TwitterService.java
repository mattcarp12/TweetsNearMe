package org.matt.tweetsnearme.Network;


import android.location.Location;
import android.util.Log;

import org.matt.tweetsnearme.Model.OAuthToken;
import org.matt.tweetsnearme.Model.Search;
import org.matt.tweetsnearme.Model.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    /*public static void getToken() {
        if (token == null) {
            try {
                token = twitterApi.postCredentials("client_credentials").execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public static void getToken() {
        twitterApi.postCredentials("client_credentials")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OAuthToken>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OAuthToken oAuthToken) {
                        token = oAuthToken;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*public static List<Tweet> getTweets(Location currLoc, int radius, int maxTweets) {
        ArrayList<Tweet> tweetList = new ArrayList<>();
        String geoCodeString = currLoc.getLatitude() + "," +
                currLoc.getLongitude() + "," +
                radius + "mi";
        try {
            Response<Search> response = twitterApi.getTweets(
                    geoCodeString, maxTweets, 1).execute();
            if (response.isSuccessful() && response.body() != null) {
                tweetList = (ArrayList) response.body().getTweets();
                return tweetList;
            } else {
                Log.d(TAG, "Twitter request unsuccessful");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tweetList;
    }*/

    public static List<Tweet> getTweets(Location currLoc, int radius, int maxTweets) {
        ArrayList<Tweet> tweetList = new ArrayList<>();
        String geoCodeString = currLoc.getLatitude() + "," +
                currLoc.getLongitude() + "," +
                radius + "mi";
        twitterApi.getTweets(geoCodeString, maxTweets, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Search>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Search search) {
                        tweetList = (ArrayList) search.getTweets();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
    }
}
