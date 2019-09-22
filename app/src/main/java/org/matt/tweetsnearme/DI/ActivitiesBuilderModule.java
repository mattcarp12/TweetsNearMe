package org.matt.tweetsnearme.DI;

import org.matt.tweetsnearme.UI.MainActivity;
import org.matt.tweetsnearme.UI.SplashScreenActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector
    abstract SplashScreenActivity contributesSplashScreenActivity();

    @ContributesAndroidInjector(modules = {TweetViewModelModule.class})
    abstract MainActivity contributeMainActivity();

}
