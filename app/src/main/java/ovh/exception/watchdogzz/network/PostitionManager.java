package ovh.exception.watchdogzz.network;

import android.Manifest;
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
 * Provide GPS location
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
                User currentUser = thisActivity.getUsers().getMe();
                currentUser.setPosition(new GPSPosition((float) location.getLongitude(),
                        (float) location.getLatitude(),
                        (float) location.getAltitude()));
                thisActivity.getUsers().updateUserPosition(currentUser.getId(), currentUser);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        this.start();
    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
    }

    public void stop() {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

}
