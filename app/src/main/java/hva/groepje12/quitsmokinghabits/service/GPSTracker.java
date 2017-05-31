package hva.groepje12.quitsmokinghabits.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.LocationData;
import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;

public class GPSTracker extends Service implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 3;
    private final Context context;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isRunning = false;
    Location location;

    public GPSTracker(Context context) {
        this.context = context;
        start();
    }

    public boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public void start() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isNetworkEnabled && !isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                );

                isRunning = true;
                Log.d("Location:Network", "Received location from network");

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                );

                isRunning = true;
                Log.d("Location:GPS", "received location from GPS");

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location changedLocation) {
        location = changedLocation;

        Profile profile = DataHolder.getCurrentProfile(context);

        ArrayList<LocationData> locations = profile.getLocations();

        for (LocationData locationData : locations) {
            Location profileLocation = locationData.getLocation();
            if (location.distanceTo(profileLocation) <= 30 && locationData.shouldSendNotification()) {
                Intent destination = new Intent(context, MainActivity.class);
                destination.putExtra("aantalRokenPopup", true);

                Intent removeIntent = new Intent(context, MainActivity.class);
                removeIntent.putExtra("removeLocation", true);
                removeIntent.putExtra("locationId", locations.indexOf(locationData));

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 12345, removeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action action = new NotificationCompat.Action(
                        R.drawable.location_off_black,
                        "Verwijder Locatie",
                        pendingIntent
                );

                Notification notification = new Notification(
                        "Ingesteld plek gevonden!",
                        "Hoi " + profile.getFirstName() + ", je bent op een ingestelde plek, laten we gaan beginnen!",
                        destination,
                        context
                );
                notification.addAction(action);
                notification.startNotification(Notification.GPS_NOTIFICATION);

                locations.remove(locationData);
                locationData.setLastAccessed(new Date());
                locations.add(locationData);
            }
        }

        profile.setLocations(locations);
        DataHolder.saveProfileToPreferences(context, profile);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
