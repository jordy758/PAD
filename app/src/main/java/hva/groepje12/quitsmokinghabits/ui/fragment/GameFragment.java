package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.ui.activity.SelectAppActivity;
import hva.groepje12.quitsmokinghabits.util.AppInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.Utilities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class GameFragment extends Fragment {


    private ListView mListAppInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.games_fragment_main, container, false);

        final Context context = getContext();

        mListAppInfo = (ListView) rootView.findViewById(R.id.appListView);
        // create new adapter
        final PackageManager pm = getActivity().getPackageManager();
        final List appsList = Utilities.getInstalledApplication(context);


        Collections.sort(appsList, new Comparator<ApplicationInfo>() {


            @Override
            public int compare(ApplicationInfo one, ApplicationInfo two) {
                return one.loadLabel(pm).toString().compareTo(two.loadLabel(pm).toString());
            }
        });


        AppInfoAdapter adapter = new AppInfoAdapter(context, appsList, getActivity().getPackageManager());


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
                Utilities.launchApp(parent.getContext(), getActivity().getPackageManager(), appInfo.packageName);
            }
        });

        //OnClick
        final Intent myIntent = new Intent(getActivity(), SelectAppActivity.class);

        ImageView app1 = (ImageView) rootView.findViewById(R.id.appIcon1);
        ImageView app2 = (ImageView) rootView.findViewById(R.id.appIcon2);
        ImageView app3 = (ImageView) rootView.findViewById(R.id.appIcon3);
        ImageView app4 = (ImageView) rootView.findViewById(R.id.appIcon4);
        ImageView app5 = (ImageView) rootView.findViewById(R.id.appIcon5);

        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent.putExtra("appNumber", "1");
                GameFragment.this.startActivity(myIntent);

            }
        });

        app2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myIntent.putExtra("appNumber", "2");
                GameFragment.this.startActivity(myIntent);
            }
        });

        app3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent.putExtra("appNumber", "3");
                GameFragment.this.startActivity(myIntent);
            }
        });

        app4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent.putExtra("appNumber", "4");
                GameFragment.this.startActivity(myIntent);
            }
        });

        app5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent.putExtra("appNumber", "5");
                GameFragment.this.startActivity(myIntent);
            }
        });
        return rootView;
    }

    public void onResume() {
        super.onResume();

        String appPackage;
        Drawable icon;

        final Context context = getContext();
        ArrayList<ImageView> appIcons = new ArrayList<ImageView>();

        appIcons.add((ImageView) getView().findViewById(R.id.appIcon1));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon2));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon3));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon4));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon5));

        for (int i = 0; i < 5; i++) {
            try {
                appPackage = PreferenceManager.getDefaultSharedPreferences(context).getString("App" + (i + 1), null);
                if (appPackage == null) {
                    appIcons.get(i).setImageResource(R.drawable.add);
                } else {
                    icon = context.getPackageManager().getApplicationIcon(appPackage);
                    appIcons.get(i).setImageDrawable(icon);
                }
            } catch (Exception e) {

            }

        }

    }
}