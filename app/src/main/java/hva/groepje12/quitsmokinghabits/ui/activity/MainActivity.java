package hva.groepje12.quitsmokinghabits.ui.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.LocationData;
import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.service.DataHolder;
import hva.groepje12.quitsmokinghabits.service.GPSTracker;
import hva.groepje12.quitsmokinghabits.ui.fragment.AlarmFragment;
import hva.groepje12.quitsmokinghabits.ui.fragment.GameFragment;
import hva.groepje12.quitsmokinghabits.ui.fragment.GoalFragment;
import hva.groepje12.quitsmokinghabits.ui.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final int HOME_POS = 0;
    private static final int DOELEN_POS = 1;
    private static final int AFLEIDING_POS = 2;
    private static final int TIJDEN_POS = 3;

    public static String ALARMS_VIEW = "alarms";
    public static String GOALS_VIEW = "goals";
    private static String view;

    private FloatingActionButton fab;

    public static String getView() {
        return view;
    }

    @Override
    protected void onPause() {
        super.onPause();

        GPSTracker gpsTracker = DataHolder.getGpsTracker(this);
        if (!gpsTracker.hasPermissions()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        }

        if (DataHolder.getCurrentProfile(this).getLocations().size() == 0) {
            gpsTracker.stop();
        } else {
            if (!gpsTracker.isRunning()) {
                gpsTracker.start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Profile profile = DataHolder.getCurrentProfile(this);

        if (profile.getFirstName() == null) {
            Intent registerIntent = new Intent(getBaseContext(), RegisterActivity.class);
            startActivity(registerIntent);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case DOELEN_POS:
                        view = GOALS_VIEW;
                        fab.setImageResource(R.drawable.add_white);
                        fab.show();
                        break;
                    case TIJDEN_POS:
                        view = ALARMS_VIEW;
                        fab.setImageResource(R.drawable.alarm_add_white);
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.getBoolean("aantalRokenPopup", false)) {
            hoeveelGerooktPopup(mViewPager);
            getIntent().removeExtra("aantalRokenPopup");
        }

        if (extras != null && extras.getBoolean("removeLocation", false)) {
            int locationId = extras.getInt("locationId", -1);

            getIntent().removeExtra("removeLocation");
            getIntent().removeExtra("locationId");

            if (locationId < 0) {
                return;
            }

            ArrayList<LocationData> locationDataArrayList = profile.getLocations();
            locationDataArrayList.remove(locationId);

            profile.setLocations(locationDataArrayList);
            DataHolder.saveProfileToPreferences(this, profile);

            if (profile.getLocations().size() < 1) {
                GPSTracker gpsTracker = DataHolder.getGpsTracker(this);
                gpsTracker.stop();
            }

            Toast.makeText(this, "Locatie is verwijderd!", Toast.LENGTH_SHORT).show();

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(Notification.GPS_NOTIFICATION);
        }
    }

    private void hoeveelGerooktPopup(final ViewPager mViewPager) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Hoeveel Gerookt?");
        alertDialogBuilder.setMessage("Vul het aantal gerookte sigaretten sinds de vorige keer in.");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(45, 0, 45, 0);

        final EditText aantalGerookt = new EditText(this);
        aantalGerookt.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(aantalGerookt, params);

        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Afleiding", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int amount = Integer.parseInt(aantalGerookt.getText().toString());

                RequestParams params = new RequestParams();
                params.put("amount", amount);

                Task addSmokeDataTask = new Task(new OnLoopJEvent() {
                    @Override
                    public void taskCompleted(JSONObject results) {
                        Random randomGenerator = new Random();
                        Profile profile = DataHolder.getCurrentProfile(MainActivity.this);

                        if (profile.getGames() == null || profile.getGames().size() == 0) {
                            mViewPager.setCurrentItem(AFLEIDING_POS);

                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Geen apps ingesteld!");
                            alertDialog.setMessage("Stel alsjeblieft wat apps in, zodat we deze voor " +
                                    "jou kunnen starten wanneer je afgeleid wilt worden!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            alertDialog.show();
                            return;
                        }

                        String randomApp = profile.getGames().get(
                                randomGenerator.nextInt(profile.getGames().size())
                        );

                        Intent destination = getPackageManager().getLaunchIntentForPackage(randomApp);
                        startActivity(destination);
                    }

                    @Override
                    public void taskFailed(JSONObject results) {
                    }

                    @Override
                    public void fatalError(String results) {
                    }
                });

                addSmokeDataTask.execute(Task.ADD_SMOKE_DATA, params);
            }
        });

        alertDialogBuilder.setCancelable(true).setNegativeButton("Terug", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // make keyboard automatically show
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case HOME_POS:
                    return new HomeFragment();
                case DOELEN_POS:
                    return new GoalFragment();
                case AFLEIDING_POS:
                    return new GameFragment();
                case TIJDEN_POS:
                    return new AlarmFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                case 1:
                    return "DOELEN";
                case 2:
                    return "AFLEIDING";
                case 3:
                    return "TIJDEN";
            }
            return null;
        }
    }
}
