package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Game;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.activity.SelectAppActivity;
import hva.groepje12.quitsmokinghabits.util.GameInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class GameFragment extends Fragment {

    private Context context;

    private ListView recommendedGames;

    private ArrayList<Game> gamesList;
    private ArrayList<ImageView> preferredAppList;
    private ArrayList<String> gameStrings;

    private Intent selectAppIntent;
    private PackageManager packageManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.games_fragment_main, container, false);
        context = getContext();

        gamesList = new ArrayList<>();
        preferredAppList = new ArrayList<>();

        LinearLayout preferredAppLayout = (LinearLayout) rootView.findViewById(R.id.preferred_app_layout);
        for (int i = 0; i < preferredAppLayout.getChildCount(); i++) {
            View view = preferredAppLayout.getChildAt(i);

            if (view instanceof ImageView) {
                preferredAppList.add((ImageView) view);
            }
        }

        recommendedGames = (ListView) rootView.findViewById(R.id.recommended_game_list);

        selectAppIntent = new Intent(getActivity(), SelectAppActivity.class);
        packageManager = context.getPackageManager();

        Game candyCrush = new Game(
                "Candy Crush",
                "Een kleurrijk puzzelspel met verschillende blokken in een drop down achtige beleving.",
                "com.king.candycrushsaga",
                getResources().getDrawable(R.drawable.candy_crush)
        );
        addRecommendedGame(candyCrush);

        Game instagram = new Game(
                "Instagram",
                "Instagram is een eenvoudige manier om de gebeurtenissen in de wereld om je heen vast te leggen en te delen.",
                "com.instagram.android",
                getResources().getDrawable(R.drawable.instagram)
        );
        addRecommendedGame(instagram);

        Game subwaySurfers = new Game(
                "Subway Surfers",
                "Help Jake, Tricky & Fresh om te ontsnappen uit handen van de barse inspecteur en zijn hond.",
                "com.kiloo.subwaysurf",
                getResources().getDrawable(R.drawable.subwaysurfers)
        );
        addRecommendedGame(subwaySurfers);

        Game clashRoyale = new Game(
                "Clash Royale",
                "Een realtime multiplayerspel ontworpen met aristocraten en nog veel, veel meer.",
                "com.supercell.clashroyale",
                getResources().getDrawable(R.drawable.clashroyale)
        );
        addRecommendedGame(clashRoyale);

        Game colorSwitch = new Game(
                "Color Switch",
                "Tik de bal zorgvuldig door elk obstakel en je bal verandert van kleur met sommige power-ups.",
                "com.fortafygames.colorswitch",
                getResources().getDrawable(R.drawable.colorswitch)
        );
        addRecommendedGame(colorSwitch);

        Game superMario = new Game(
                "Super Mario Run",
                "Een nieuw soort Super Mario-game, die je met één hand kunt spelen!",
                "com.nintendo.zara",
                getResources().getDrawable(R.drawable.supermariorun)
        );
        addRecommendedGame(superMario);

        showRecommendedGames();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showFavoriteApps();
    }

    private void showFavoriteApps() {
        ProfileManager profileManager = new ProfileManager(context);
        Profile profile = profileManager.getCurrentProfile();

        gameStrings = profile.getGames();

        for (int i = 0; i < (gameStrings == null ? 0 : gameStrings.size()); i++) {
            try {
                Drawable icon = context.getPackageManager().getApplicationIcon(gameStrings.get(i));
                preferredAppList.get(i).setImageDrawable(icon);
            } catch (Exception e) {
            }
        }

        for (final ImageView app : preferredAppList) {
            app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = preferredAppList.indexOf(app);

                    if (gameStrings.size() <= index) {
                        chooseApp(preferredAppList.indexOf(app));
                        return;
                    }

                    String packageName = gameStrings.get(index);
                    Intent intent = packageManager.getLaunchIntentForPackage(packageName);

                    if (intent == null) {
                        chooseApp(index);
                        return;
                    }

                    startActivity(packageManager.getLaunchIntentForPackage(packageName));
                }
            });

            app.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    chooseApp(preferredAppList.indexOf(app));
                    return true;
                }
            });
        }
    }

    private void chooseApp(int appNumber) {
        selectAppIntent.putExtra("appNumber", appNumber);
        startActivity(selectAppIntent);
    }

    private void addRecommendedGame(Game game) {
        gamesList.add(game);

        for (Game gameInList : gamesList) {
            if (isPackageInstalled(gameInList.getPackageName(), packageManager)) {
                gamesList.remove(gameInList);
            }
        }
    }

    private void showRecommendedGames() {
        GameInfoAdapter gameInfoAdapter = new GameInfoAdapter(context, gamesList);
        recommendedGames.setAdapter(gameInfoAdapter);

        recommendedGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game selectedGame = (Game) adapterView.getItemAtPosition(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + selectedGame.getPackageName())));
            }
        });
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}