package org.matt.tweetsnearme.Utilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import org.matt.tweetsnearme.R;

public class TweetMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private final View tweetView;

    public TweetMarkerAdapter(Context context) {
        tweetView = LayoutInflater.from(context).inflate(R.layout.tweet_view, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, tweetView);
        return tweetView;
    }

    private void render(Marker marker, View view) {
        Tweet tweet = (Tweet) marker.getTag();
        ((TextView) view.findViewById(R.id.tweet_username)).setText(tweet.user.name);
        ((TextView) view.findViewById(R.id.tweet_distance)).setText("5");
        ((TextView) view.findViewById(R.id.tweet_text)).setText(tweet.text);
        Picasso.get()
                .load(tweet.user.profileImageUrlHttps)
                .into((ImageView) view.findViewById(R.id.tweet_author_image), new MarkerCallback(marker));

    }

    static class MarkerCallback implements Callback {
        Marker marker = null;

        MarkerCallback(Marker marker) {
            this.marker = marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
    }
}
