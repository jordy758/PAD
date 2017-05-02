package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.util.AppInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.Utilities;

public class GameFragment extends Fragment {



        private ListView mListAppInfo;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.games_fragment_main, container, false);

            Context context = getContext();

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


            return rootView;
        }
    }