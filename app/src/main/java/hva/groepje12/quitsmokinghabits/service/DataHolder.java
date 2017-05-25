package hva.groepje12.quitsmokinghabits.service;

import android.content.Context;

import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class DataHolder {
    private static GPSTracker gpsTracker;
    private static ProfileManager profileManager;
    private static Profile profile;

    public static GPSTracker getGpsTracker(Context context) {
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
        }

        return gpsTracker;
    }

    public static ProfileManager getProfileManager(Context context) {
        if (profileManager == null) {
            profileManager = new ProfileManager(context);
        }

        return profileManager;
    }

    public static Profile getCurrentProfile(Context context) {
        ProfileManager profileManager = getProfileManager(context);

        if (profile == null) {
            profile = profileManager.getCurrentProfile();
        }

        return profile;
    }

    public static void saveProfileToPreferences(Context context, Profile profile) {
        DataHolder.profile = profile;

        ProfileManager profileManager = getProfileManager(context);
        profileManager.saveToPreferences(profile);
    }
}
