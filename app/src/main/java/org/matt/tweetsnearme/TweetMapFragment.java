package org.matt.tweetsnearme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.models.Tweet;

public class TweetMapFragment extends Fragment implements OnMapReadyCallback {


    private OnFragmentInteractionListener mListener;
    private static final String TAG = TweetMapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private MainActivity mainActivity;
    private LatLng mLatLng;

    public TweetMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(TweetMapFragment.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: show radius of 1 mile where tweets will be located
        // TODO: Set altitude properly so that zooms into 1 mile radius

        Log.d(TAG, "Map is ready!");
        mMap = googleMap;
        setMapPosition();
        addMapMarkers();
    }

    private void setMapPosition() {
        mLatLng = new LatLng(mainActivity.mLocation.getLatitude(),
                             mainActivity.mLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
    }

    private void addMapMarkers() {
        // TODO: Make custom map marker for tweets
        // TODO: Custom marker should show tweet, username, and distance from current location


        for (Tweet tweet : mainActivity.tweetList) {
            if (tweet.coordinates != null)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tweet.coordinates.getLatitude(), tweet.coordinates.getLongitude()))
                        .title("Title")
                        .snippet(tweet.text));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
