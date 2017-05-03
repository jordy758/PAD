package hva.groepje12.quitsmokinghabits.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import hva.groepje12.quitsmokinghabits.receiver.AlarmReceiver;

public class TimedNotification {

    private int hour, minutes, seconds;

    private Context context;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Notification notification;
    private SharedPreferences sharedPreferences;

    public TimedNotification(Context context, Notification notification, int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;

        this.context = context;
        this.notification = notification;
        this.sharedPreferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);

        startTimer();
    }

    public TimedNotification(Context context, Notification notification, int hour, int minutes, int seconds) {
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;

        this.context = context;
        this.notification = notification;
        this.sharedPreferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);

        startTimer();
    }

    public void startTimer() {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
