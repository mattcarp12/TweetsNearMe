package org.matt.tweetsnearme.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Repository.TweetRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TweetViewModel extends ViewModel {

    private TweetRepository tweetRepository;
    private MutableLiveData<List<Tweet>> tweetList;

    public TweetViewModel(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public MutableLiveData<List<Tweet>> getTweetList() {
        if (tweetList == null) {
            tweetList = new MutableLiveData<List<Tweet>>();
        }
        updateTweetList();
        return tweetList;
    }

    public void updateTweetList() {
        tweetRepository
                .getTweets()
                .subscribe(new SingleObserver<List<Tweet>>() {
                    @Override
                    public void onSuccess(List<Tweet> tweets) {
                        tweetList.setValue(tweets);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }
}
