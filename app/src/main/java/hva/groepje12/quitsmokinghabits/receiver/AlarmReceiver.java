package hva.groepje12.quitsmokinghabits.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = new Notification("Quit smoking habits", "Bla test", MainActivity.class, context);
        notification.startNotification(Notification.ALARM_NOTIFICATION);
    }

}
