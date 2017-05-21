package hva.groepje12.quitsmokinghabits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private int appNumber;

    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }

        context = getApplicationContext();

        ListView appListView = (ListView) findViewById(R.id.SelectAppListView);

        packageManager = getPackageManager();
        List appList = Utilities.getInstalledApplication(context);


        Collections.sort(appList, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo one, ApplicationInfo two) {
                return one.loadLabel(packageManager).toString().compareTo(two.loadLabel(packageManager).toString());
            }
        });


        AppInfoAdapter adapter = new AppInfoAdapter(context, appList, getPackageManager());
        Intent intent = getIntent();
        appNumber = intent.getIntExtra("appNumber", 0);

        // set adapter to list view
        appListView.setAdapter(adapter);
        // implement event when an item on list view is selected
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // get the list adapter
                AppInfoAdapter appInfoAdapter = (AppInfoAdapter) parent.getAdapter();
                // get selected item on the list
                ApplicationInfo appInfo = (ApplicationInfo) appInfoAdapter.getItem(pos);
                // launch the selected application

                ProfileManager profileManager = new ProfileManager(context);
                Profile profile = profileManager.getCurrentProfile();

                ArrayList<String> games = profile.getGames();
                if (games == null) {
                    games = new ArrayList<>();
                }

                if (games.size() > appNumber) {
                    games.set(appNumber, appInfo.packageName);
                } else if (!games.contains(appInfo.packageName)) {
                    games.add(appInfo.packageName);
                }

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
