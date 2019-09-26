package org.matt.tweetsnearme.ViewModel;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Repository.TweetRepository;

import java.util.List;

import javax.inject.Inject;

public class TweetViewModel extends AndroidViewModel {

    private static final String TAG = TweetViewModel.class.getSimpleName();
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweetList;
    private MutableLiveData<Location> currentLocation;

    @Inject
    public TweetViewModel(Application application, TweetRepository tweetRepository) {
        super(application);
        this.tweetRepository = tweetRepository;
        tweetList = tweetRepository.getTweets();
        currentLocation = new MutableLiveData<>();
        update(false);
    }

    public LiveData<List<Tweet>> getTweetList() {
        return tweetList;
    }

    public void updateTweetList(boolean forceUpdate) {
        tweetRepository.refreshTweets(forceUpdate);
    }

    public MutableLiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    private void updateCurrentLocation() {
        tweetRepository
                .getLocation()
                .subscribe(location -> currentLocation.setValue(location),
                        e -> e.printStackTrace());
    }

    public void update(boolean forceUpdate) {
        updateCurrentLocation();
        updateTweetList(forceUpdate);
    }
}
