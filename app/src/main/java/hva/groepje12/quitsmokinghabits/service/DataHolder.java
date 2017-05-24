package hva.groepje12.quitsmokinghabits.service;

import android.content.Context;

public class DataHolder {
    private static GPSTracker gpsTracker;

    public static GPSTracker getGpsTracker(Context context) {
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
        }

        return gpsTracker;
    }
}
