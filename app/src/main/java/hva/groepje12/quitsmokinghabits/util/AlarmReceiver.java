package hva.groepje12.quitsmokinghabits.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hva.groepje12.quitsmokinghabits.model.Notification;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = new Notification("Bla test", context);
        notification.startNotification();
    }

}
