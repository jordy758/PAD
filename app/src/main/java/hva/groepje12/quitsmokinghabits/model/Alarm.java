package hva.groepje12.quitsmokinghabits.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import hva.groepje12.quitsmokinghabits.util.AlarmReceiver;

/**
 * Created by Lucas van Leijen
 */

public class Alarm {

    private int hour;
    private int minutes;

    private Context context;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public Alarm(int hour, int minutes, Context context) {
        this.hour = hour;
        this.minutes = minutes;
        this.context = context;
    }

    public void startAlarm() {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
