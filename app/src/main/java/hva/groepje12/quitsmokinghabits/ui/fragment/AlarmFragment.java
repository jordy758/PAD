package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;

import hva.groepje12.quitsmokinghabits.R;

public class AlarmFragment extends Fragment {

    public TextView time;
    TimePicker TimePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void set_text(String output) {
        time.setText(output);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.alarms_fragment_main, container, false);

        time = (TextView) rootView.findViewById(R.id.time);
        //TimePicker = (TimePicker) rootView.findViewById(R.id.TimePicker);
        //TimePicker.setIs24HourView(true);
        final Calendar calendar = Calendar.getInstance();

        Button add_time = (Button) rootView.findViewById(R.id.add_time);

        /* create an onClick listener to start the alarm
        add_time.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, TimePicker.getHour());
                calendar.set(Calendar.MINUTE, TimePicker.getMinute());

                // get the int values of the hour and minute
                int hour = TimePicker.getHour();
                int minute = TimePicker.getMinute();

                // convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // method that changes the update text Textbox
                set_text(" " + hour_string + ":" + minute_string);

            }

        }); */
        return rootView;
    }



    public void timePicker(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "time");
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);


            TimePickerDialog dpd = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
                    (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, true);

            dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return dpd;
        }
    }

}
