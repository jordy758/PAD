package hva.groepje12.quitsmokinghabits.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hva.groepje12.quitsmokinghabits.R;

public class AppInfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<ApplicationInfo> mListAppInfo;
    private PackageManager mPackManager;

    public AppInfoAdapter(Context c, List<ApplicationInfo> list, PackageManager pm) {
        mContext = c;
        mListAppInfo = list;
        mPackManager = pm;
    }

    @Override
    public int getCount() {
        return mListAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mListAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApplicationInfo entry = mListAppInfo.get(position);
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.listview_apps, null);
        }

        ImageView ivAppIcon = (ImageView) v.findViewById(R.id.appIcon);
        TextView tvAppName = (TextView) v.findViewById(R.id.appName);

        ivAppIcon.setImageDrawable(entry.loadIcon(mPackManager));
        tvAppName.setText(entry.loadLabel(mPackManager));

        return v;
    }
}
