package ovh.exception.watchdogzz.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import ovh.exception.watchdogzz.activities.MainActivity;
import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.User;

/**
 * Created by begarco on 20/11/2016.
 */

public class PostitionManager {
    // Acquire a reference to the system Location Manager
    private LocationManager locationManager;
    // Define a listener that responds to location updates
    private LocationListener locationListener;

    private Context context;
    private MainActivity thisActivity;

    public PostitionManager(MainActivity context) {
        this.context = context;
        this.thisActivity = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("POSITION GPS", location.toString());
                thisActivity.getUsers().updateUser(thisActivity.getUsers().getMe().getId(),
                        new User(0,"","",true,new GPSPosition(  (float)location.getLatitude(),
                                                                (float)location.getLongitude(),
                                                                (float)location.getAltitude())));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }


}
