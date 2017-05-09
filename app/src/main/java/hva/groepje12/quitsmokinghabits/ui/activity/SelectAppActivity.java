package hva.groepje12.quitsmokinghabits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.AppInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;
import hva.groepje12.quitsmokinghabits.util.Utilities;

public class SelectAppActivity extends AppCompatActivity {
    private Context context;
    private ListView mListAppInfo;
    private List appsList;
    private int appNumber;

    private PackageManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {

        }

        context = getApplicationContext();

        mListAppInfo = (ListView) findViewById(R.id.SelectAppListView);

        pm = getPackageManager();
        appsList = Utilities.getInstalledApplication(context);


        Collections.sort(appsList, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo one, ApplicationInfo two) {
                return one.loadLabel(pm).toString().compareTo(two.loadLabel(pm).toString());
            }
        });


        AppInfoAdapter adapter = new AppInfoAdapter(context, appsList, getPackageManager());
        Intent intent = getIntent();
        appNumber = Integer.parseInt(intent.getStringExtra("appNumber"));

        // set adapter to list view
        mListAppInfo.setAdapter(adapter);
        // implement event when an item on list view is selected
        mListAppInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // get the list adapter
                AppInfoAdapter appInfoAdapter = (AppInfoAdapter) parent.getAdapter();
                // get selected item on the list
                ApplicationInfo appInfo = (ApplicationInfo) appInfoAdapter.getItem(pos);
                // launch the selected application

                ProfileManager profileManager = new ProfileManager(context);
                Profile profile = profileManager.getCurrentProfile();

                List<String> games = profile.getGames();
                if (games == null) {
                    games = new ArrayList<String>();
                }

                if (games.size() > appNumber) {
                    games.remove(appNumber - 1);
                }

                games.add(appInfo.packageName);


                profile.setGames(games);
                profileManager.saveToPreferences(profile);

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
