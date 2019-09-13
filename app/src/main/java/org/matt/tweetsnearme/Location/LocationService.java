package org.matt.tweetsnearme.Location;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class LocationService {

    private static final String TAG = LocationService.class.getSimpleName();
    private Location mLocation;
    private Boolean mLocationPermissionGranted;
    private FusedLocationProviderClient fusedLocationClient;

    public LocationService(Context context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.mLocationPermissionGranted = mLocationPermissionGranted;
    }


    public Location getCurrentLocation() {
        if (mLocationPermissionGranted) {
            try {
                Log.d(TAG, "Getting actual device location.");
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    Log.d(TAG, "Location retreval successful.");
                                    mLocation = location;
                                } else {
                                    // TODO: Request current location if null
                                    Log.d(TAG, "Last known location is null, setting to default");
                                    setDefaultLocation();
                                }
                            }
                        });
            } catch (SecurityException e) {
                Log.d("EXCEPTION: ", e.getMessage());
            }
        } else {
            setDefaultLocation();
        }
        return mLocation;
    }

    private void setDefaultLocation() {
        Log.d(TAG, "Setting location to default, Googleplex");
        mLocation = new Location("");
        mLocation.setLatitude(37.422);
        mLocation.setLongitude(-122.084);
    }
}
