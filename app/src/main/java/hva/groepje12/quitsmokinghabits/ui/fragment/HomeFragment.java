package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.TimedNotification;
import hva.groepje12.quitsmokinghabits.model.Notification;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_main, container, false);

        final Notification notify = new Notification("Klik hier om afgeleid te wordennn", getActivity().getApplicationContext());
        final TimedNotification alarm = new TimedNotification(00, 00, getActivity().getApplicationContext(), notify);

        Button buttonNotify = (Button) rootView.findViewById(R.id.button_send_notification);
        buttonNotify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notify.startNotification();
            }
        });

        Button buttonAlarm = (Button) rootView.findViewById(R.id.button_send_timed_notification);
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minutes = calendar.get(Calendar.MINUTE);

                alarm.setHour(hour);
                alarm.setMinutes(minutes);
                alarm.startTimer();
            }
        });

        return rootView;
    }


}
