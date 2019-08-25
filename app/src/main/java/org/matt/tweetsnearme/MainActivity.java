package org.matt.tweetsnearme;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.twitter.sdk.android.core.models.Tweet;

import org.matt.tweetsnearme.Utilities.TwitterService;

import java.util.List;

public class MainActivity extends AppCompatActivity //FragmentActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLocation;
    private LatLng mLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Tweet> tweetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Code for nav drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_refresh:
                getCurrentLocation();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (id) {
            case R.id.nav_tweet_map:
                fragment = new TweetMapFragment();
                break;
            case R.id.nav_tweet_list:
                fragment = new TweetListFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_content, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if (ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Log.d(TAG, "permission already given");
            //getCurrentLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "permission previously denied, requesting permission");
                new AlertDialog.Builder(this)
                        .setTitle("Request location permission")
                        .setMessage("Location permission is required to retreive tweets near you!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                Log.d(TAG, "permission not previously denied, requesting permission");
                ActivityCompat.requestPermissions(MainActivity.this,
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
                                    updateMapWithLocationAndTweets();
                                } else {
                                    // TODO: Request current location if null
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
        updateMapWithLocationAndTweets();
    }

    private void updateMapWithLocationAndTweets() {
        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(mLatLng).title(mLatLng.latitude + ", " + mLatLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));


        // TODO: show radius of 1 mile where tweets will be located
        // TODO: Set altitude properly so that zooms into 1 mile radius

        // Once initial location has been set, make call to get tweets
        new TwitterQueryTask().execute(mLatLng);
    }

    // TODO: Create functionality to refresh tweets and reload map and/or RecycleView list
    // TODO: Create functionality to set preferences for radius, number of tweets to show, etc.

    public class TwitterQueryTask extends AsyncTask<LatLng, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // TODO: Set loading indicator to visible

        }

        @Override
        protected void onPostExecute(Void voidObj) {
            super.onPostExecute(voidObj);

            // TODO: As soon as loading is complete, hide the loading indicator

            // Completed: If query results are valid, show markers on map
            // TODO: Make custom map marker for tweets
            // TODO: Custom marker should show tweet, username, and distance from current location

            for (Tweet tweet : tweetList) {
                if (tweet.coordinates != null)
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(tweet.coordinates.getLatitude(), tweet.coordinates.getLongitude()))
                            .title("Title")
                            .snippet(tweet.text));
            }
        }

        @Override
        protected Void doInBackground(LatLng... mLatLng) {
            TwitterService.getToken();
            tweetList = TwitterService.getTweets(mLatLng[0], 1, 100);
            return null;
        }


    }

}
