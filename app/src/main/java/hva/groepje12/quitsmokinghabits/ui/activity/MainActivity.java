package hva.groepje12.quitsmokinghabits.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.fragment.AlarmFragment;
import hva.groepje12.quitsmokinghabits.ui.fragment.GameFragment;
import hva.groepje12.quitsmokinghabits.ui.fragment.HomeFragment;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileManager = new ProfileManager(this);
        final Profile profile = profileManager.getCurrentProfile();

        if (profile.getFirstName() != null) {
            Toast.makeText(this, "Welkom " + profile.getFullName() + "!", Toast.LENGTH_LONG).show();
        } else {
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
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
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
                    case 2:
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Hoeveel Gerookt?");
            alertDialogBuilder.setMessage("Vul het aantal gerookte sigaretten sinds de vorige keer in.");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(45, 0, 45, 0);

            final EditText aantalGerookt = new EditText(this);
            aantalGerookt.setInputType(InputType.TYPE_CLASS_NUMBER);

            layout.addView(aantalGerookt, params);

            alertDialogBuilder.setView(layout);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Afleiding", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Use this amount to work with the statistics
                    int aantal = Integer.parseInt(aantalGerookt.getText().toString());

                    Random randomGenerator = new Random();

                    Profile profile = profileManager.getCurrentProfile();
                    String randomApp = profile.getGames().get(
                            randomGenerator.nextInt(profile.getGames().size())
                    );

                    Intent destination = getPackageManager().getLaunchIntentForPackage(randomApp);
                    startActivity(destination);
                }
            });

            alertDialogBuilder.setCancelable(true).setNegativeButton("Terug", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            // make keyboard automatically show
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            alertDialog.show();
        }
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
                case 0:
                    return new HomeFragment();
                case 1:
                    return new GameFragment();
                case 2:
                    return new AlarmFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                case 1:
                    return "GAMES";
                case 2:
                    return "ALARMEN";
            }
            return null;
        }
    }
}
