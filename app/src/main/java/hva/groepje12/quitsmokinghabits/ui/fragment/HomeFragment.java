package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.service.DataHolder;

public class HomeFragment extends Fragment {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private View rootView;
    private TextView notSmokedForTextView, todaySmokedTextView, moneySavedTextView, cigarettesNotSmokedTextView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) != 0) {
                    return;
                }
                try {
                    Date date = simpleDateFormat.parse(notSmokedForTextView.getText().toString());

                    Calendar myDate = Calendar.getInstance();
                    myDate.setTime(date);
                    myDate.add(Calendar.MINUTE, 1);

                    notSmokedForTextView.setText(simpleDateFormat.format(myDate.getTime()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        };

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_main, container, false);

        //Assign value to last cigarette
        notSmokedForTextView = (TextView) rootView.findViewById(R.id.timeNotSmokedTextView);
        todaySmokedTextView = (TextView) rootView.findViewById(R.id.tv_cigarettesSmokedToday);
        moneySavedTextView = (TextView) rootView.findViewById(R.id.tv_moneySaved);
        cigarettesNotSmokedTextView = (TextView) rootView.findViewById(R.id.cigarettesNotSmokedTodayTextView);

        return rootView;
    }

    private void fillTiles() {
        RequestParams params = new RequestParams();

        Task notSmokedForTask = new Task(new OnLoopJEvent() {
            @Override
            public void taskCompleted(JSONObject results) {
                try {
                    JSONObject tileData = results.getJSONObject("message");

                    String todaySmoked = tileData.getString("smokedToday");

                    String notSmokedFor = tileData.getString("notSmokedFor");
                    notSmokedFor = notSmokedFor.equals("No data found!") ? "Geen Data" : notSmokedFor;

                    String cigarettesSaved = tileData.getString("cigarettesSaved");
                    String savedMoney = tileData.getString("savedMoney");

                    JSONArray perfectLine = tileData.getJSONArray("perfectLine");
                    JSONArray myLine = tileData.getJSONArray("myLine");

                    ArrayList<Integer> perfectLineArrayList = new ArrayList<>();
                    ArrayList<Integer> myLineArrayList = new ArrayList<>();
                    for (int i = 0; i < perfectLine.length(); i++) {
                        perfectLineArrayList.add(perfectLine.getInt(i));
                    }

                    for (int i = 0; i < myLine.length(); i++) {
                        myLineArrayList.add(myLine.getInt(i));
                    }

                    if (myLine.length() == 0) {
                        Profile profile = DataHolder.getCurrentProfile(getActivity());
                        myLineArrayList.add(profile.getCigarettesPerDay());
                    }

                    createChart(perfectLineArrayList, myLineArrayList);

                    todaySmokedTextView.setText(todaySmoked);
                    notSmokedForTextView.setText(notSmokedFor);
                    cigarettesNotSmokedTextView.setText(cigarettesSaved);
                    moneySavedTextView.setText(savedMoney);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void taskFailed(JSONObject results) {
            }

            @Override
            public void fatalError(String results) {
            }
        });

        notSmokedForTask.execute(Task.GET_TILE_DATA, params);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillTiles();
    }

    public void createChart(List<Integer> perfectLine, List<Integer> myLine) {
        LineChart lineChart = (LineChart) rootView.findViewById(R.id.smokeChart);

        final ArrayList<Entry> myEntries = new ArrayList<>();
        int i = 0;
        for(Integer dat : myLine) {
            myEntries.add(new Entry((i++), dat));
        }

        final ArrayList<Entry> perfectEntries = new ArrayList<>();
        i = 0;
        for(Integer dat : perfectLine) {
            perfectEntries.add(new Entry((i++), dat));
        }

        List<ILineDataSet> lines = new ArrayList<>();
        LineDataSet myEntriesLine = new LineDataSet(myEntries, "Mijn lijn");
        LineDataSet perfectEntriesLine = new LineDataSet(perfectEntries, "Afbouw Lijn");
        myEntriesLine.setColor(Color.RED);
        myEntriesLine.setFillColor(Color.RED);
        myEntriesLine.setDrawFilled(true);
        myEntriesLine.setDrawCircles(false);
        myEntriesLine.setDrawValues(false);
        perfectEntriesLine.setColor(Color.GREEN);
        perfectEntriesLine.setFillColor(Color.GREEN);
        perfectEntriesLine.setDrawFilled(true);
        perfectEntriesLine.setDrawCircles(false);
        perfectEntriesLine.setDrawValues(false);
        lines.add(myEntriesLine);
        lines.add(perfectEntriesLine);

        lineChart.setTouchEnabled(false);
        lineChart.getXAxis().setEnabled(false);
//        lineChart.getAxisLeft().setEnabled(false);
//        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setText("");
        lineChart.setData(new LineData(lines));
        lineChart.invalidate();
        lineChart.animate();
    }

}