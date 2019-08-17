package org.matt.tweetsnearme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;
import org.matt.tweetsnearme.Utilities.TweetUtility;

import java.net.URL;

import twitter4j.JSONArray;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLocation;
    private LatLng mLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private TweetUtility tweetUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        tweetUtility = new TweetUtility(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map is ready!");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng tampa = new LatLng(27.94752, -82.45843);// 27.9506° N, 82.4572° W
//        mMap.addMarker(new MarkerOptions().position(tampa).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(tampa));

        Log.d(TAG, "Asking for permission");
        getLocationPermission();

        if (mLocationPermissionGranted) getCurrentLocation();
    }

    public void getLocationPermission() {
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(MapsActivity.this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Log.d(TAG, "permission already given");
            //getCurrentLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "permission previously denied, requesting permission");
                new AlertDialog.Builder(this)
                        .setTitle("Request location permission")
                        .setMessage("Location permission is required to retreive tweets near you!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                Log.d(TAG, "permission not previously denied, requesting permission");
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted.", Toast.LENGTH_SHORT).show();
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "Permission request granted.");
                } else {
                    Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                    mLocationPermissionGranted = false;
                    Log.d(TAG, "Permission request denied.");
                }
            }
        }
        getCurrentLocation();
    }


    private void getCurrentLocation() {
        if (mLocationPermissionGranted) {
            try {
                Log.d(TAG, "Getting actual device location.");
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    Log.d(TAG, "Location retreval successful.");
                                    mLocation = location;
                                    updateMapWithLocation();
                                } else {
                                    Log.d(TAG, "Last known location is null, setting to default");
                                    setDefaultLocation();
                                }
                            }
                        });
            } catch(SecurityException e) {
                Log.d("EXCEPTION: ",  e.getMessage());
            }
        } else {
            setDefaultLocation();
        }


    }

    private void setDefaultLocation() {
        Log.d(TAG, "Setting location to default, Googleplex");
        mLocation = new Location("");
        mLocation.setLatitude(37.422);
        mLocation.setLongitude(-122.084);
        updateMapWithLocation();
    }

    private void updateMapWithLocation() {
        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mLatLng).title(mLatLng.latitude + ", " + mLatLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));


        // TODO: show radius of 1 mile where tweets will be located

        // Once initial location has been set, make call to get tweets
        new TwitterQueryTask().execute(mLatLng);


    }


    public class TwitterQueryTask extends AsyncTask<LatLng, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // TODO: Set loading indicator to visible

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // TODO: As soon as loading is complete, hide the loading indicator

            // TODO: If query results are valid, show markers on map
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Your first tweet, Sir.")
                    .setMessage(s)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do nothing
                        }
                    })
                    .create()
                    .show();
        }

        @Override
        protected String doInBackground(LatLng... mLatLng) {
            return tweetUtility.tweetQuery(mLatLng[0]);
        }
    }

}
