package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class HomeFragment extends Fragment {

    private List<String> games;
    private View rootView;
    private Profile profile;
    private TextView notSmokedForTextView, todaySmokedTextView, moneySavedTextView, cigarettesNotSmokedTextView;

    private ArrayList<String> quoteList = new ArrayList<String>();
    int max = 4;
    int min = 0;
    Random random = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_main, container, false);

        //Add inspiring dutch Quotes here
        quoteList.add("0");
        quoteList.add("1");
        quoteList.add("2");
        quoteList.add("3");
        quoteList.add("4");

        //Assign value to last cigarette
        notSmokedForTextView = (TextView) rootView.findViewById(R.id.tv_timeNotSmoked);
        todaySmokedTextView = (TextView) rootView.findViewById(R.id.tv_cigarettesSmokedToday);
        moneySavedTextView = (TextView) rootView.findViewById(R.id.tv_moneySaved);
        cigarettesNotSmokedTextView = (TextView) rootView.findViewById(R.id.tv_cigarettesNotSmokedToday);

        ProfileManager profileManager = new ProfileManager(getContext());
        profile = profileManager.getCurrentProfile();

        if (profile == null) {
            return rootView;
        }

        TextView welcome = (TextView) rootView.findViewById(R.id.welcomeTextView);
        welcome.append(profile.getFirstName());

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillTiles();
            }
        });

        fillTiles();
        createChart();

        return rootView;
    }

    private void fillTiles() {
        RequestParams params = new RequestParams();
        params.add("notification_token", profile.getNotificationToken());

        Task notSmokedForTask = new Task(new OnLoopJEvent() {
            @Override
            public void taskCompleted(JSONObject results) {
                try {
                    JSONObject tileData = results.getJSONObject("message");

                    String todaySmoked = tileData.getString("smokedToday");
                    todaySmoked = todaySmoked.equals("No smoke data found!") ? "Geen Data" : todaySmoked;

                    String notSmokedFor = tileData.getString("notSmokedFor");
                    String cigarettesSaved = tileData.getString("cigarettesSaved");
                    String savedMoney = tileData.getString("savedMoney");

                    todaySmokedTextView.setText(todaySmoked);
                    notSmokedForTextView.setText(notSmokedFor);
                    cigarettesNotSmokedTextView.setText(cigarettesSaved);
                    moneySavedTextView.setText(savedMoney);
                } catch (JSONException exception) {
                }
            }

            @Override
            public void taskFailed(JSONObject results) {}

            @Override
            public void fatalError(String results) {}
        });

        notSmokedForTask.execute(Task.GET_TILE_DATA, params);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView TVQuote = (TextView) rootView.findViewById(R.id.motivationText);
        TVQuote.setText(getRandomQuote());
    }

    public void createChart() {

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart);
        //adding data
        final ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 2));
        entries.add(new Entry(2, 12));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 14));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 26));

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("Ma");
        labels.add("Di");
        labels.add("Wo");
        labels.add("Do");
        labels.add("Vr");
        labels.add("Za");
        labels.add("Zo");

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        };

        IValueFormatter dataFormat = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int) value + "";
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        LineDataSet dataset = new LineDataSet(entries, "aantal sigaretten");

        LineData data = new LineData(dataset);

        lineChart.setData(data);
        lineChart.getDescription().setText("");
        lineChart.setTouchEnabled(false);
        dataset.setColors(Color.RED);
        data.setValueTextSize(10);
        data.setValueFormatter(dataFormat);
    }

    public String getRandomQuote() {
        int randomNumber = random.nextInt(max - min + 1) + min;
        return quoteList.get(randomNumber);
    }


}