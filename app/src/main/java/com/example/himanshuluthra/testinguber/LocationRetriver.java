package com.example.himanshuluthra.testinguber;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by himanshuluthra on 08/03/17.
 */

public class LocationRetriver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    public static final int REQUEST_PERMISSION = 0;
    public static final int REQUEST_CHECK_SETTINGS = 1;
    public static final int REQUEST_RESOLVE_ERROR = 2;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private LocationCallback mLocationCallback;

    private static LocationRetriver mInstance = null;

    private LocationRetriver(Activity activity) {
        this.mActivity = activity;
        mLocationCallback = null;
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public static LocationRetriver getInstance(Activity activity) {
        if(mInstance == null) {
            mInstance = new LocationRetriver(activity);
        }
        return mInstance;
    }

    public void onConnected(Bundle var1) {
        getLocation();
    }

    public void onConnectionSuspended(int var1) {

    }

    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(mActivity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {

            }
        } else {
            onUpdateLocation(false, null);
        }
    }

    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can initialize location requests here.
                getLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                    status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                    onUpdateLocation(false, null);
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                onUpdateLocation(false, null);
                break;
        }
    }

    public void permissionRequestResult(boolean isGranted) {
        if(isGranted) {
            getLocation();
        } else {
            Toast.makeText(mActivity, "Permission Required For App To Work", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getLocation();
            } else {
                onUpdateLocation(true, null);
            }
        } else if(requestCode == REQUEST_RESOLVE_ERROR && resultCode == Activity.RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    private void getLocation() {
        if(!isPermissionAlreadyGranted(Manifest.permission.ACCESS_COARSE_LOCATION, mActivity)) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//
//            }
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        } else if(mGoogleApiClient.isConnected()) {
            try {
                LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastLocation == null) {
                    checkSettings();
                } else {
                    onUpdateLocation(true, lastLocation);
                }
            } catch (SecurityException e) {

            }
        } else if(!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    public void getLocation(LocationCallback callback) {
        mLocationCallback = callback;
        getLocation();
    }

    private static Boolean isPermissionAlreadyGranted(String permissionConst, Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permissionConst);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void onUpdateLocation(boolean res, Location location) {
        if(mLocationCallback != null) {
            mLocationCallback.locationResult(res, location);
        }
    }

    private void checkSettings() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); // accuracy of 100 meters
        //locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER); // not working
        //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // not desirable

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }
}
