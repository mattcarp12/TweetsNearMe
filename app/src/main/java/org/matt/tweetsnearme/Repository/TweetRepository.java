package org.matt.tweetsnearme.Repository;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.matt.tweetsnearme.Database.TweetDao;
import org.matt.tweetsnearme.Database.TweetDatabase;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Network.TwitterService;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class TweetRepository {

    private static final String TAG = TweetRepository.class.getSimpleName();
    private static final int FRESH_TIMEOUT_IN_MINUTES = 3;
    private TweetDao tweetDao;
    private TwitterService twitterService;
    private Boolean mLocationPermissionGranted;
    private FusedLocationProviderClient fusedLocationClient;
    private Date lastTweetUpdate;
    private Context context;


    public TweetRepository(Application application) {
        this.context = application;
        this.tweetDao = TweetDatabase.getDatabase(application).tweetDao();
        this.twitterService = new TwitterService();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
        this.mLocationPermissionGranted = ContextCompat.checkSelfPermission(application,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweetDao.getTweets();
    }

    public void refreshTweets(boolean forceRefresh) {
        Date currDate = new Date();
        if (lastTweetUpdate == null
                || (currDate.getTime() - lastTweetUpdate.getTime()) / 60000 > FRESH_TIMEOUT_IN_MINUTES
                || forceRefresh) {
            refreshTweetDatabase();
            lastTweetUpdate = currDate;
        }
    }

    private void refreshTweetDatabase() {
        Observable.fromCallable(() -> {
            return tweetDao.deleteAll();
        })
                .flatMap(completable -> {
                    completable.subscribe();
                    return twitterService.getTweets(getLocation(), 5, 100);
                })
                .flatMap(tweets -> {
                    tweets.forEach(tweet -> Picasso.with(context).load(tweet.getUser().getProfileImageUrl()));
                    return Observable.fromCallable(() -> tweetDao.insertAll(tweets));
                })
                .subscribeOn(Schedulers.io())
                .subscribe(completable -> completable.subscribe());
    }

    private Location getDefaultLocation() {
        Location mLocation = new Location("");
        mLocation.setLatitude(37.422);
        mLocation.setLongitude(-122.084);
        return mLocation;
    }

    public Single<Location> getLocation() {
        return Single.create(emitter -> {
            if (!mLocationPermissionGranted) emitter.onSuccess(getDefaultLocation());
            else {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            Location currLoc = location != null ? location : getDefaultLocation();
                            try {
                                emitter.onSuccess(currLoc);
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

}
