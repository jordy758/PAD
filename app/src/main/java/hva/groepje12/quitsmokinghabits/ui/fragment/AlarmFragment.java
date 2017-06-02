package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Alarm;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.service.DataHolder;
import hva.groepje12.quitsmokinghabits.ui.activity.GoogleMapsActivity;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;

public class AlarmFragment extends Fragment {

    private Button button;
    private ListView timesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Alarm> alarms;
    private ArrayList<String> alarmStrings;

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

        final Profile profile = DataHolder.getCurrentProfile(getContext());

        Button maps = (Button) alarmView.findViewById(R.id.startMaps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoogleMapsActivity.class));
            }
        });

        alarms = profile.getAlarms();
        alarmStrings = new ArrayList<>();

        for (Alarm alarm : alarms) {
            alarmStrings.add(alarm.getTime());
        }

        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, alarmStrings);

        timesListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.getView().equals(MainActivity.ALARMS_VIEW)) {
                    return;
                }

                // Get Current time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        final String timeString = simpleDateFormat.format(time.getTime());

                        Task addTimeTask = new Task(new OnLoopJEvent() {
                            @Override
                            public void taskCompleted(JSONObject results) {
                                try {
                                    JSONObject response = results.getJSONObject("response");

                                    Gson gson = new GsonBuilder().create();
                                    Alarm alarm = gson.fromJson(
                                            response.getJSONObject("alarm").toString(),
                                            Alarm.class
                                    );

                                    alarms.add(alarm);
                                    alarmStrings.add(timeString);
                                    adapter.notifyDataSetChanged();
                                    updateProfileAndList();

                                    Snackbar.make(alarmView, "Tijd toegevoegd!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } catch (JSONException ex) {
                                }
                            }

                            @Override
                            public void taskFailed(JSONObject results) {
                                Snackbar.make(alarmView, "Tijd bestaat al!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void fatalError(String results) {
                            }
                        });

                        RequestParams params = new RequestParams();
                        params.add("notification_time", timeString);

                        addTimeTask.execute(Task.ADD_TIME, params);
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
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String time = (String) timesListView.getItemAtPosition(position);

                Task task = new Task(new OnLoopJEvent() {
                    @Override
                    public void taskCompleted(JSONObject results) {
                        alarmStrings.remove(time);
                        alarms.remove(position);

                        updateProfileAndList();
                    }

                    @Override
                    public void taskFailed(JSONObject results) {
                        Toast.makeText(getActivity(), "Failed to remove entry!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fatalError(String results) {
                    }
                });

                RequestParams params = new RequestParams();
                params.add("id", Integer.toString(alarms.get(position).getId()));
                task.execute(Task.REMOVE_TIME, params);

                return true;
            }
        });

        return alarmView;
    }


    private void updateProfileAndList() {
        Profile profile = DataHolder.getCurrentProfile(getContext());
        profile.setAlarms(alarms);
        DataHolder.saveProfileToPreferences(getContext(), profile);
        adapter.notifyDataSetChanged();
    }

}
