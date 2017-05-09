package hva.groepje12.quitsmokinghabits.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Map<String, String> data = remoteMessage.getData();

        Intent destination = new Intent(getBaseContext(), MainActivity.class);
        destination.putExtra("aantalRokenPopup", true);

        Notification notification = new Notification(data.get("title"),
                data.get("text"), destination, getApplicationContext());
        notification.startNotification();

    }
}
