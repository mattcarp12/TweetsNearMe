package org.matt.tweetsnearme;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.twitter.sdk.android.core.models.Tweet;

import org.matt.tweetsnearme.Utilities.TwitterService;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TweetMapFragment.OnFragmentInteractionListener,
        TweetListFragment.OnFragmentInteractionListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Location mLocation;
    private FusedLocationProviderClient fusedLocationClient;
    List<Tweet> tweetList;
    private Integer currentFragment;

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
        getLocationPermission();

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
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        currentFragment = itemId;
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }



    public void getLocationPermission() {
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Log.d(TAG, "permission already given");
            getCurrentLocation();
        } else { // If don't currently have permission, then request permission
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
                                    getTweetList();
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
        getTweetList();
    }

    private void getTweetList() {
        new TwitterQueryTask().execute(mLocation);
    }


    // TODO: Create functionality to refresh tweets and reload map and/or RecycleView list
    // TODO: Create functionality to set preferences for radius, number of tweets to show, etc.

    public class TwitterQueryTask extends AsyncTask<Location, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: Set loading indicator to visible
        }

        @Override
        protected void onPostExecute(Void voidObj) {
            super.onPostExecute(voidObj);
            // TODO: As soon as loading is complete, hide the loading indicator
            if (currentFragment == null) displaySelectedScreen(R.id.nav_tweet_map);
        }

        @Override
        protected Void doInBackground(Location... mLocation) {
            TwitterService.getToken();
            tweetList = TwitterService.getTweets(mLocation[0], 1, 100);
            return null;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
