package com.dmitrytyunkov.simpleweather.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.dmitrytyunkov.simpleweather.view.WeatherView;

public class CurrentLoccation {
    @SuppressLint("MissingPermission")
    public static void getLocation(LocationManager locationManager, final WeatherView weatherView) {
        String locationProvider;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationProvider = LocationManager.GPS_PROVIDER;
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locationProvider = LocationManager.NETWORK_PROVIDER;
        else {
            weatherView.returnLocationError("Location offline");
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                weatherView.returnLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestSingleUpdate(locationProvider, locationListener, null);
    }
}
