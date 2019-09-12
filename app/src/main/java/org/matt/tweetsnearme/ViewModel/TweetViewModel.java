package org.matt.tweetsnearme.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Repository.TweetRepository;

import java.util.List;

public class TweetViewModel extends ViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweetList;

    public TweetViewModel(Context context, TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    public void init() {
        tweetList = LiveDataReactiveStreams.fromPublisher(tweetRepository.getTweets());
    }

}
