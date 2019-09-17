package org.matt.tweetsnearme.ViewModel;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Repository.TweetRepository;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private MutableLiveData<List<Tweet>> tweetList;
    private MutableLiveData<Location> currentLocation;

    public TweetViewModel(Application application) {
        super(application);
        this.tweetRepository = new TweetRepository(application);
    }

    public MutableLiveData<List<Tweet>> getTweetList() {
        if (tweetList == null) {
            tweetList = new MutableLiveData<>();
        }
        updateTweetList();
        return tweetList;
    }

    private void updateTweetList() {
        tweetRepository
                .getTweets()
                .subscribe(tweets -> tweetList.setValue(tweets),
                        e -> e.printStackTrace());
    }

    public MutableLiveData<Location> getCurrentLocation() {
        if (currentLocation == null) {
            currentLocation = new MutableLiveData<>();
        }
        updateCurrentLocation();
        return currentLocation;
    }

    private void updateCurrentLocation() {
        tweetRepository
                .getLocation()
                .subscribe(location -> currentLocation.setValue(location));
    }

    public void update() {
        updateCurrentLocation();
        updateTweetList();
    }
}
