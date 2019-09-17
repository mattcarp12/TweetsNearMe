package org.matt.tweetsnearme.Repository;

import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.matt.tweetsnearme.Database.TweetDao;
import org.matt.tweetsnearme.Database.TweetDatabase;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Network.TwitterService;

import java.util.List;

import io.reactivex.Single;


public class TweetRepository {

    private static final String TAG = TweetRepository.class.getSimpleName();
    private TweetDao tweetDao;
    private TwitterService twitterService;
    private Boolean mLocationPermissionGranted;
    private FusedLocationProviderClient fusedLocationClient;


    public TweetRepository(Application application) {
        this.tweetDao = TweetDatabase.getDatabase(application).tweetDao();
        this.twitterService = new TwitterService();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
        this.mLocationPermissionGranted = ContextCompat.checkSelfPermission(application,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public Single<List<Tweet>> getTweets() {
        return twitterService.getTweets(getLocation(), 5, 100);
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
