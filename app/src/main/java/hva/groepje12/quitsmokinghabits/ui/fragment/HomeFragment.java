package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;
import hva.groepje12.quitsmokinghabits.util.Utilities;

public class HomeFragment extends Fragment {

    private List<String> games;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_main, container, false);

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

        Drawable icon;

        final Context context = getContext();
        final PackageManager packageManager = getActivity().getPackageManager();

        final ArrayList<ImageView> preferredAppList = new ArrayList<ImageView>();

        LinearLayout preferredAppLayout = (LinearLayout) rootView.findViewById(R.id.preferred_app_layout);
        for (int i = 0; i < preferredAppLayout.getChildCount(); i++) {
            View view = preferredAppLayout.getChildAt(i);

            if (view instanceof ImageView) {
                preferredAppList.add((ImageView) view);
            }
        }

        ProfileManager profileManager = new ProfileManager(context);
        Profile profile = profileManager.getCurrentProfile();

        games = profile.getGames();

        for (int i = 0; i < (games == null ? 0 : games.size()); i++) {
            try {
                icon = context.getPackageManager().getApplicationIcon(games.get(i));
                preferredAppList.get(i).setImageDrawable(icon);
            } catch (Exception e) {
            }
        }

        for (final ImageView app : preferredAppList) {
            app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilities.launchApp(context, packageManager, games.get(preferredAppList.indexOf(app)));
                }
            });
        }

    }

}