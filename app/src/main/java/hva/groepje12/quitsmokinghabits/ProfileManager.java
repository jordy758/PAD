package hva.groepje12.quitsmokinghabits;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

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
        return gson.fromJson(prefs.getString("profile", ""), Profile.class);
    }
}
