package hva.groepje12.quitsmokinghabits.model;

import android.location.Location;
import android.location.LocationManager;

import java.util.Date;

public class LocationData {
    private float accuracy, speed;
    private double longitude, latitude;
    private String provider;

    private Date lastAccessed;

    private long MAX_DURATION = 30 * 60 * 1000;

    public Location getLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }

    public void setLocation(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public boolean shouldSendNotification() {
        if (lastAccessed == null) {
            return true;
        }

        return (new Date().getTime() - this.lastAccessed.getTime()) >= MAX_DURATION;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
