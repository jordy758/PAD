package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import hva.groepje12.quitsmokinghabits.exceptions.ProfileNotFoundException;
import hva.groepje12.quitsmokinghabits.model.Profile;

public class ProfileManager {

    private SharedPreferences prefs;

    public ProfileManager(Context context) {
        prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }

    public void save(Profile profile) {
        Editor prefEditor = prefs.edit();
        Gson gson = new Gson();

        prefEditor.putString("profile", gson.toJson(profile));
        prefEditor.apply();
    }

    public Profile getProfile() throws ProfileNotFoundException {
        String profileJson = prefs.getString("profile", "");

        if (profileJson.equals("")) {
            throw new ProfileNotFoundException();
        }

        return new Gson().fromJson(profileJson, Profile.class);
    }
}
