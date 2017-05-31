package hva.groepje12.quitsmokinghabits.model;

import android.location.Location;
import android.location.LocationManager;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LocationData {
    private double longitude, latitude;
    private Date lastAccessed;
    private ArrayList<Calendar> times;

    private long MAX_DURATION = 30 * 60 * 1000;

    public LocationData() {
        this.times = new ArrayList<>();
    }

    public void addTime(Calendar time) {
        this.times.add(time);
    }

    public ArrayList<Calendar> getTimes() {
        return this.times;
    }

    public void setTimes(ArrayList<Calendar> times) {
        this.times = times;
    }

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

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
