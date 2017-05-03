package hva.groepje12.quitsmokinghabits.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lucas on 1-5-2017.
 */

public class Utilities {


    public static List<ApplicationInfo> getInstalledApplication(Context c) {
        List<ApplicationInfo> appList = c.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        final PackageManager pm = c.getPackageManager();

        for (int i = 0; i < appList.size(); i++) {
            ApplicationInfo ai = null;
            try {
                ai = pm.getApplicationInfo(appList.get(i).packageName, 0);
            } catch (Exception e) {
            }

            if (appList.get(i).loadLabel(pm) == appList.get(i).packageName) {
                appList.remove(i);
            }


            if ((ai.flags & ApplicationInfo.FLAG_IS_GAME) != ApplicationInfo.FLAG_IS_GAME) {
                Log.e("PAD: ", appList.get(i).packageName + "");
                appList.remove(i);

            }

        }


        return appList;
    }

    /*
     * Launch an application
     * @param	c	Context of application
     * @param	pm	the related package manager of the context
     * @param	pkgName	Name of the package to run
     */
    public static boolean launchApp(Context c, PackageManager pm, String pkgName) {
        // query the intent for lauching
        if(pkgName == null) {
            return false;
        }

        Intent intent = pm.getLaunchIntentForPackage(pkgName);
        // if intent is available
        if (intent != null) {
            try {
                // launch application
                c.startActivity(intent);
                // if succeed
                return true;

                // if fail
            } catch (ActivityNotFoundException ex) {
                // quick message notification
                Toast toast = Toast.makeText(c, "Application Not Found", Toast.LENGTH_LONG);
                // display message
                toast.show();
            }
        }
        // by default, fail to launch
        return false;
    }
}
