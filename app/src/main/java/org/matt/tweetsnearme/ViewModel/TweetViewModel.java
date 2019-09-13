package org.matt.tweetsnearme.ViewModel;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Repository.TweetRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private MutableLiveData<List<Tweet>> tweetList;
    private Location mLocation;

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

    public Location getLocation() {
        return mLocation;
    }

    public void updateTweetList() {
        tweetRepository
                .getTweets()
                .subscribe(new SingleObserver<List<Tweet>>() {
                    @Override
                    public void onSuccess(List<Tweet> tweets) {
                        tweetList.setValue(tweets);
                        updateLocation();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    private void updateLocation() {
        mLocation = tweetRepository.getLocation();
    }
}
