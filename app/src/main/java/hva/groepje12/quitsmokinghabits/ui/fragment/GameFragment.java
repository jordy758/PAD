package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
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
import hva.groepje12.quitsmokinghabits.model.Game;
import hva.groepje12.quitsmokinghabits.ui.activity.SelectAppActivity;
import hva.groepje12.quitsmokinghabits.util.AppInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.GameInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.Utilities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class GameFragment extends Fragment {


    private ListView mListAppInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.games_fragment_main, container, false);

        final Context context = getContext();


        ListView recommendedGames = (ListView) rootView.findViewById(R.id.recommendGameList);
        ArrayList<Game> gamesList = new ArrayList<Game>();
        //adding games
        Drawable candyCrush = getResources().getDrawable(R.drawable.candycrush);
        gamesList.add(new Game("Candy Crush", "Een kleurrijk puzzelspel met verschillende blokken in een drop down achtige beleving met allemaal leuke dingen en je kan ook veel geld besteden.", "com.king.candycrushsaga", candyCrush));
        gamesList.add(new Game("Candy Test", "Test", "com.king.test", candyCrush));



        //removing installed games from list
        final PackageManager pm = context.getPackageManager();
        for(int i = 0; i < gamesList.size();) {
            if(isPackageInstalled(gamesList.get(i).getPackageName(), pm)){
                gamesList.remove(i);

                
            }
            else {
                i++;
            }
        }

        GameInfoAdapter gameInfo = new GameInfoAdapter(context, gamesList);
        recommendedGames.setAdapter(gameInfo);


        recommendedGames.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game selectedGame = (Game) adapterView.getItemAtPosition(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + selectedGame.getPackageName())));
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

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}