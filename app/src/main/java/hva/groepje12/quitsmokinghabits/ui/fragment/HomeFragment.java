package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.model.TimedNotification;
import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;
import hva.groepje12.quitsmokinghabits.util.Utilities;

public class HomeFragment extends Fragment {

    private List<String> games;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_main, container, false);
        final Context context = getContext();

        final Notification notify = new Notification("Quit Smoking Habits", "Klik hier om afgeleid te wordennn",
                MainActivity.class, getActivity().getApplicationContext());

        ProfileManager profileManager = new ProfileManager(getContext());
        final Profile profile = profileManager.getCurrentProfile();
        TextView welcome = (TextView) rootView.findViewById(R.id.welcomeTextView);

        if (profile != null && profile.getFirstName() != null) {
            welcome.append(profile.getFirstName());
        }


        return rootView;
    }

    public void onResume() {
        super.onResume();

        String appPackage;
        Drawable icon;

        final Context context = getContext();
        final PackageManager pm = getActivity().getPackageManager();

        ArrayList<ImageView> appIcons = new ArrayList<ImageView>();
        final ArrayList<String> packageList = new ArrayList<String>();

        ImageView app1 = (ImageView) getView().findViewById(R.id.appIcon1);
        ImageView app2 = (ImageView) getView().findViewById(R.id.appIcon2);
        ImageView app3 = (ImageView) getView().findViewById(R.id.appIcon3);
        ImageView app4 = (ImageView) getView().findViewById(R.id.appIcon4);
        ImageView app5 = (ImageView) getView().findViewById(R.id.appIcon5);

        appIcons.add((ImageView) getView().findViewById(R.id.appIcon1));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon2));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon3));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon4));
        appIcons.add((ImageView) getView().findViewById(R.id.appIcon5));

        ProfileManager profileManager = new ProfileManager(context);
        Profile profile = profileManager.getCurrentProfile();

        games = profile.getGames();

        for (int i = 0; i < (games == null ? 0 : games.size()); i++) {
            try {
                icon = context.getPackageManager().getApplicationIcon(games.get(i));
                appIcons.get(i).setImageDrawable(icon);
            } catch (Exception e) {}
        }

        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.launchApp(context, pm, games.get(0));

            }
        });

        app2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utilities.launchApp(context, pm, games.get(1));
            }
        });

        app3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.launchApp(context, pm, games.get(2));
            }
        });

        app4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.launchApp(context, pm, games.get(3));
            }
        });

        app5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.launchApp(context, pm, games.get(4));
            }
        });


    }

}