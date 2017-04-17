package hva.groepje12.quitsmokinghabits.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.ui.activity.RegisterActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Lucas van Leijen
 */

public class Notification {

    private String text;
    private Context context;

    public Notification(String text, Context context) {
        this.text = text;
        this.context = context;
    }

    public void startNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cig)
                        .setContentTitle("Quit Smoking Habits")
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 200, 200, 200, 200 })
                        .setLights(Color.CYAN, 3000, 3000);

        Intent resultIntent = new Intent(context, RegisterActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
