package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import hva.groepje12.quitsmokinghabits.model.Profile;

public class ProfileManager {

    private Context context;
    SharedPreferences prefs;

    public ProfileManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }

    public void save(Profile profile) {
        Editor prefEditor = prefs.edit();
        Gson gson = new Gson();

        prefEditor.putString("profile", gson.toJson(profile));
        prefEditor.commit();
    }

    public Profile getProfile() {
        Gson gson = new Gson();
        String profileJson = prefs.getString("profile", "");
        return profileJson == "" ? new Profile() : gson.fromJson(profileJson, Profile.class);
    }
}
