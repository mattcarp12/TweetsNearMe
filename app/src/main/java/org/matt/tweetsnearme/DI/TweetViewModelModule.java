package org.matt.tweetsnearme.DI;

import androidx.lifecycle.ViewModel;

import org.matt.tweetsnearme.ViewModel.TweetViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TweetViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TweetViewModel.class)
    public abstract ViewModel bindTweetViewModel(TweetViewModel tweetViewModel);
}
