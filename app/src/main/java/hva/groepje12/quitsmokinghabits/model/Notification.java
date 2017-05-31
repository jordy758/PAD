package hva.groepje12.quitsmokinghabits.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notification {

    public static int FIREBASE_NOTIFICATION = 1;
    public static int ALARM_NOTIFICATION = 2;
    public static int GPS_NOTIFICATION = 3;
    private String title, text;
    private Context context;
    private Class<?> internDestination;
    private Intent intentDestination;
    private Action action;

    public Notification(String title, String text, Class<?> internDestination, Context context) {
        this.title = title;
        this.text = text;
        this.context = context;
        this.internDestination = internDestination;
    }

    public Notification(String title, String text, Intent intentDestination, Context context) {
        this.title = title;
        this.text = text;
        this.context = context;
        this.intentDestination = intentDestination;
    }

    public void addAction(Action action) {
        this.action = action;
    }

    public void startNotification(int notificationNumber) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.smoke_free_white)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{200, 200, 200, 200})
                        .setLights(Color.CYAN, 3000, 3000);

        if (action != null) {
            mBuilder.addAction(action);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);

        if (intentDestination != null) {
            resultIntent = intentDestination;
        }

        if (internDestination != null) {
            resultIntent = new Intent(context, internDestination);
        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationNumber, mBuilder.build());

    }

    public void setContext(Context context) {
        this.context = context;
    }

}
