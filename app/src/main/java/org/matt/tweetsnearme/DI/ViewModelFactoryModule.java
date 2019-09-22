package org.matt.tweetsnearme.DI;

import androidx.lifecycle.ViewModelProvider;

import org.matt.tweetsnearme.ViewModel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);

}
