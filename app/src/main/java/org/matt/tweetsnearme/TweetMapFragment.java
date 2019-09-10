package org.matt.tweetsnearme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Utilities.TweetMarkerAdapter;

public class TweetMapFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private OnFragmentInteractionListener mListener;
    private GoogleMap.InfoWindowAdapter infoWindowAdapter;
    private static final String TAG = TweetMapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private MainActivity mainActivity;
    private LatLng mLatLng;
    private TweetMarkerAdapter tweetMarkerAdapter;

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
        tweetMarkerAdapter = new TweetMarkerAdapter(mainActivity);
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
        mMap.setInfoWindowAdapter(tweetMarkerAdapter);
        mMap.setOnInfoWindowClickListener(this);
        setMapPosition();
        addMapMarkers();
    }

    private void setMapPosition() {
        mLatLng = new LatLng(mainActivity.mLocation.getLatitude(),
                             mainActivity.mLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
    }

    private void addMapMarkers() {
        // TODO: Make custom map marker for tweets
        // TODO: Custom marker should show tweet, username, and distance from current location


        for (Tweet tweet : mainActivity.tweetList) {
            if (tweet.getCoordinates() != null)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(tweet.getCoordinates().getLatitude(), tweet.getCoordinates().getLongitude()))
                ).setTag(tweet);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

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
