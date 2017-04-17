package hva.groepje12.quitsmokinghabits.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hva.groepje12.quitsmokinghabits.ui.notification.Notification;

/**
 * Created by Lucas van Leijen
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Notification notify = new Notification("Klik hier om afgeleid te worden", context);

        notify.startNotification();
    }
}
