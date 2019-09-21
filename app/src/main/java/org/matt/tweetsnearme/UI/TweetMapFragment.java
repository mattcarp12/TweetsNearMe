package org.matt.tweetsnearme.UI;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.matt.tweetsnearme.Adapters.TweetMarkerAdapter;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.R;
import org.matt.tweetsnearme.ViewModel.TweetViewModel;

import java.util.List;

public class TweetMapFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private static final String TAG = TweetMapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private TweetMarkerAdapter tweetMarkerAdapter;
    private TweetViewModel mViewModel;
    private OnFragmentInteractionListener mListener;

    public TweetMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TweetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO : Research difference of onCreate, onCreateView, and onViewCreated.
        tweetMarkerAdapter = new TweetMarkerAdapter(getActivity());
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(TweetMapFragment.this);
        mViewModel.getTweetList().observe(this, tweets -> addMapMarkers(tweets));
        mViewModel.getCurrentLocation().observe(this, location -> setMapPosition(location));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: show radius of 1 mile where tweets will be located
        // TODO: Set altitude properly so that zooms into 1 mile radius
        mMap = googleMap;
        mMap.setInfoWindowAdapter(tweetMarkerAdapter);
        mMap.setOnInfoWindowClickListener(this);
    }

    private void setMapPosition(Location mLocation) {
        LatLng mLatLng = new LatLng(mLocation.getLatitude(),
                mLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));
    }

    private void addMapMarkers(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            if (tweet.coordinates != null)
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
