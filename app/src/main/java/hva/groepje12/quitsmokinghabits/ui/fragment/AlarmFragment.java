package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.tasks.AddTimeTask;
import hva.groepje12.quitsmokinghabits.api.tasks.RemoveTimeTask;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class AlarmFragment extends Fragment {

    private ListView timesListView;
    private ArrayAdapter<String> adapter;
    private List<String> alarms;

    private ProfileManager profileManager;
    private Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View alarmView = inflater.inflate(R.layout.alarms_fragment_main, container, false);
        View mainView = getActivity().findViewById(R.id.main_activity);

        timesListView = (ListView) alarmView.findViewById(R.id.list_times);
        timesListView.setLongClickable(true);

        profileManager = new ProfileManager(getActivity());
        profile = profileManager.getCurrentProfile();

        alarms = profile.getAlarms();

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, alarms);

        timesListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);

                        AddTimeTask addTimeTask = new AddTimeTask(new OnLoopJEvent() {
                            @Override
                            public void taskCompleted(JSONObject results) {
                                alarms.add(time);
                                adapter.notifyDataSetChanged();
                                updateProfileAndList();

                                Snackbar.make(alarmView, "Tijd toegevoegd!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void taskFailed(String results) {
                                Snackbar.make(alarmView, "Tijd bestaat al!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });

                        RequestParams params = new RequestParams();
                        params.add("notification_token", profile.getNotificationToken());
                        params.add("notification_time", time);

                        addTimeTask.execute(params);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        });

        timesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) timesListView.getItemAtPosition(position);
                Toast.makeText(getActivity(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });

        timesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String time = (String) timesListView.getItemAtPosition(position);

                RemoveTimeTask removeTimeTask = new RemoveTimeTask(new OnLoopJEvent() {
                    @Override
                    public void taskCompleted(JSONObject results) {
                        alarms.remove(time);
                        updateProfileAndList();
                    }

                    @Override
                    public void taskFailed(String results) {
                        Toast.makeText(getActivity(), "Failed to remove entry!", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestParams params = new RequestParams();
                params.add("notification_token", profile.getNotificationToken());
                params.add("notification_time", time);
                removeTimeTask.execute(params);

                return true;
            }
        });

        return alarmView;
    }

    private void updateProfileAndList() {
        profile.setAlarms(alarms);
        profileManager.saveToPreferences(profile);
        adapter.notifyDataSetChanged();
    }

}
