/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hva.groepje12.quitsmokinghabits.ui.activity;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.LocationData;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.service.DataHolder;
import hva.groepje12.quitsmokinghabits.service.GPSTracker;
import hva.groepje12.quitsmokinghabits.util.PermissionUtils;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class GoogleMapsActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Marker currentMarker = null;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> alarmStrings;
    private FloatingActionButton fab;
    private TextView informationTextView, timesInformationTextView;
    private Button removeLocationButton, linkTimesButton;
    private ListView timesListView;
    private LocationData currentLocation;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        linkTimesButton = (Button) findViewById(R.id.linkTimesButton);
        removeLocationButton = (Button) findViewById(R.id.removeLocationButton);
        informationTextView = (TextView) findViewById(R.id.informationTextView);
        timesInformationTextView = (TextView) findViewById(R.id.timesInformationTextView);
        timesListView = (ListView) findViewById(R.id.datesListView);

        alarmStrings = new ArrayList<>();
        adapter = new ArrayAdapter<>(GoogleMapsActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, alarmStrings);
        timesListView.setAdapter(adapter);
        timesListView.setLongClickable(true);

        timesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Profile profile = DataHolder.getCurrentProfile(GoogleMapsActivity.this);

                ArrayList<Calendar> times = currentLocation.getTimes();
                times.remove(position);

                ArrayList<LocationData> locationDatas = profile.getLocations();
                LocationData locationData = locationDatas.get(locationDatas.indexOf(currentLocation));
                locationData.setTimes(times);

                DataHolder.saveProfileToPreferences(GoogleMapsActivity.this, profile);
                alarmStrings.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.mapsFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.getView().equals(MainActivity.ALARMS_VIEW)) {
                    return;
                }

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(GoogleMapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        alarmStrings.add("Rond " + simpleDateFormat.format(time.getTime()) + " uur");
                        adapter.notifyDataSetChanged();

                        Profile profile = DataHolder.getCurrentProfile(getBaseContext());
                        ArrayList<LocationData> locationDatas = profile.getLocations();

                        LatLng markerPos = currentMarker.getPosition();
                        for (LocationData locationData : locationDatas) {
                            Location location = locationData.getLocation();
                            if (location.getLongitude() == markerPos.longitude && location.getLatitude() == markerPos.latitude) {
                                int index = locationDatas.indexOf(locationData);
                                locationData.addTime(time);
                                locationDatas.set(index, locationData);
                            }
                        }
                        DataHolder.saveProfileToPreferences(GoogleMapsActivity.this, profile);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        });

        linkTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.VISIBLE);
                timesListView.setVisibility(View.VISIBLE);
                linkTimesButton.setVisibility(View.INVISIBLE);
                informationTextView.setVisibility(View.INVISIBLE);
                timesInformationTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        Profile profile = DataHolder.getCurrentProfile(getBaseContext());

        for (LocationData locationData : profile.getLocations()) {
            mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(
                                    locationData.getLocation().getLatitude(),
                                    locationData.getLocation().getLongitude())
                    )
            );
        }

        GPSTracker gpsTracker = DataHolder.getGpsTracker(GoogleMapsActivity.this);
        final Location location = gpsTracker.getLocation();

        map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 9
                )
        );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Profile profile = DataHolder.getCurrentProfile(getBaseContext());
                ArrayList<LocationData> locations = profile.getLocations();
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                temp.setLatitude(latLng.latitude);
                temp.setLongitude(latLng.longitude);

                LocationData locationData = new LocationData();
                locationData.setLocation(temp);
                locations.add(locationData);
                DataHolder.saveProfileToPreferences(GoogleMapsActivity.this, profile);
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                informationTextView.setText("Wil je deze locatie koppelen aan tijden? Dan krijg je alleen een melding op die tijdstippen op die locatie");
                removeLocationButton.setVisibility(View.VISIBLE);
                linkTimesButton.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
                timesListView.setVisibility(View.INVISIBLE);
                informationTextView.setVisibility(View.VISIBLE);
                timesInformationTextView.setVisibility(View.INVISIBLE);

                Profile profile = DataHolder.getCurrentProfile(GoogleMapsActivity.this);
                alarmStrings.removeAll(alarmStrings);
                for (LocationData locationData : profile.getLocations()) {
                    if (locationData.getLocation().getLatitude() != marker.getPosition().latitude) {
                        continue;
                    }

                    if (locationData.getLocation().getLongitude() != marker.getPosition().longitude) {
                        continue;
                    }

                    currentLocation = locationData;

                    for (Calendar time : locationData.getTimes()) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        alarmStrings.add("Rond " + simpleDateFormat.format(time.getTime()) + " uur");
                    }
                }
                adapter.notifyDataSetChanged();

                currentMarker = marker;
                return false;
            }
        });

        Button removeLocation = (Button) findViewById(R.id.removeLocationButton);
        removeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = DataHolder.getCurrentProfile(getBaseContext());
                ArrayList<LocationData> locationDatas = profile.getLocations();
                Iterator<LocationData> iterator = locationDatas.iterator();

                LatLng markerPos = currentMarker.getPosition();
                while (iterator.hasNext()) {
                    LocationData locationData = iterator.next();
                    Location location = locationData.getLocation();
                    if (location.getLongitude() == markerPos.longitude && location.getLatitude() == markerPos.latitude) {
                        iterator.remove();
                    }
                }
                DataHolder.saveProfileToPreferences(GoogleMapsActivity.this, profile);
                currentMarker.remove();

                informationTextView.setVisibility(View.VISIBLE);
                informationTextView.setText("Klik op een locatie om deze aan te passen of te verwijderen. Je kan ook locaties toevoegen door op de plek te klikken!");
                removeLocationButton.setVisibility(View.INVISIBLE);
                linkTimesButton.setVisibility(View.INVISIBLE);
                timesListView.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                timesInformationTextView.setVisibility(View.INVISIBLE);
            }
        });

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
