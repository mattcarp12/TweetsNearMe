package org.matt.tweetsnearme;


import android.app.Application;

import com.squareup.picasso.Picasso;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.setSingletonInstance(
                new Picasso.Builder(this).build()
        );
    }
}
