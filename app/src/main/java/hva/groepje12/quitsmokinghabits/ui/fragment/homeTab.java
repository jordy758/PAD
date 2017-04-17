package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.ui.activity.RegisterActivity;

/**
 * Created by lucas on 17-4-2017.
 */

public class homeTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_main, container, false);

        final Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), RegisterActivity.class);
                homeTab.this.startActivity(myIntent);
            }
        });

        return rootView;
    }


}
