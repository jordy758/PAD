package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hva.groepje12.quitsmokinghabits.R;

/**
 * Created by lucas on 17-4-2017.
 */

public class alarmsTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alarms_fragment_main, container, false);
        return rootView;
    }
}
