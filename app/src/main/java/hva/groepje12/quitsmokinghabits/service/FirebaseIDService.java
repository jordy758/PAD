package hva.groepje12.quitsmokinghabits.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;


public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Profile profile = DataHolder.getCurrentProfile(getApplicationContext());
        profile.setNotificationToken(token);

        DataHolder.saveProfileToPreferences(getApplicationContext(), profile);
        ProfileManager profileManager = DataHolder.getProfileManager(getApplicationContext());

        if (profile.getFirstName() != null) {
            //Attempt to call api to register the profile
            RequestParams params = profileManager.getParams();

            Task registerProfileTask = new Task(new OnLoopJEvent() {
                @Override
                public void taskCompleted(JSONObject results) {
                }

                @Override
                public void taskFailed(JSONObject results) {
                }

                @Override
                public void fatalError(String results) {
                }
            });

            registerProfileTask.execute(Task.REGISTER_PROFILE, params);
        }
    }

}
