package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Game;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.activity.SelectAppActivity;
import hva.groepje12.quitsmokinghabits.util.GameInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class GameFragment extends Fragment {

    private Context context;
    private View rootView;

    private ImageView app1;
    private ImageView app2;
    private ImageView app3;
    private ImageView app4;
    private ImageView app5;
    private ListView recommendedGames;

    private ArrayList<Game> gamesList;
    private ArrayList<ImageView> appIcons;

    private Drawable icon;
    private Intent myIntent;
    private PackageManager pm;
    private String appPackage;

    private GameInfoAdapter gameInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.games_fragment_main, container, false);
        context = getContext();

        gamesList = new ArrayList<Game>();
        appIcons = new ArrayList<ImageView>();

        app1 = (ImageView) rootView.findViewById(R.id.appIcon1);
        app2 = (ImageView) rootView.findViewById(R.id.appIcon2);
        app3 = (ImageView) rootView.findViewById(R.id.appIcon3);
        app4 = (ImageView) rootView.findViewById(R.id.appIcon4);
        app5 = (ImageView) rootView.findViewById(R.id.appIcon5);
        recommendedGames = (ListView) rootView.findViewById(R.id.recommendGameList);

        myIntent = new Intent(getActivity(), SelectAppActivity.class);
        pm = context.getPackageManager();

        addRecommendedGames();
        showRecommendedGames();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showFavoriteApps();
    }

    private void showFavoriteApps() {
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon1));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon2));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon3));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon4));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon5));

        ProfileManager profileManager = new ProfileManager(context);
        Profile profile = profileManager.getCurrentProfile();

        List<String> games = profile.getGames();

        for (int i = 0; i < (games == null ? 0 : games.size()); i++) {
            try {
                icon = context.getPackageManager().getApplicationIcon(games.get(i));
                appIcons.get(i).setImageDrawable(icon);
            } catch (Exception e) {}
        }

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
    }

    private void addRecommendedGames() {
        Drawable candyCrush = getResources().getDrawable(R.drawable.candycrush);
        gamesList.add(
                new Game(
                    "Candy Crush",
                    "Een kleurrijk puzzelspel met verschillende blokken in een drop down " +
                    "achtige beleving met allemaal leuke dingen en je kan ook veel geld besteden.",
                    "com.king.candycrushsaga",
                    candyCrush
                )
        );

        for (int i = 0; i < gamesList.size(); ) {
            if (isPackageInstalled(gamesList.get(i).getPackageName(), pm)) {
                gamesList.remove(i);
            } else {
                i++;
            }
        }
    }

    private void showRecommendedGames() {
        gameInfo = new GameInfoAdapter(context, gamesList);
        recommendedGames.setAdapter(gameInfo);

        recommendedGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game selectedGame = (Game) adapterView.getItemAtPosition(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + selectedGame.getPackageName())));
            }
        });
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