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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class HomeFragment extends Fragment {
    private View rootView;

    private Profile profile;
    private TextView notSmokedForTextView, todaySmokedTextView, moneySavedTextView, cigarettesNotSmokedTextView;

    private ArrayList<String> quoteList = new ArrayList<>();

    private BroadcastReceiver broadcastReceiver;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());


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

        //Add inspiring dutch Quotes here
        quoteList.add("Sigaretten bevatten arseen, formaldehyde, lood, waterstofcyanide, stikstofoxiden, " +
                "koolmonoxide, ammonia en 43 bekende carcinogenen (kankerverwekkers).");
        quoteList.add("Begin jaren 50 gebruikte het sigarettenmerk Kent blauwe asbest voor het " +
                "materiaal van de filter, dit is de gevaarlijkste vorm van asbest die er is.");
        quoteList.add("Ureum, een chemische verbinding die een hoofdcomponent van urine is, wordt " +
                "gebruikt om \"smaak\" aan sigaretten te geven.");
        quoteList.add("Per jaar doet 30 procent van de rokers in Nederland één of meerdere pogingen " +
                "om te stoppen met roken.");
        quoteList.add("De filter werd oorspronkelijk uitgevonden in 1925 door de Hongaarse uitvinder " +
                "Boris Aivaz, die het proces van het maken van een sigaretfilter van crêpepapier patenteerde.");

        //Assign value to last cigarette
        notSmokedForTextView = (TextView) rootView.findViewById(R.id.tv_timeNotSmoked);
        todaySmokedTextView = (TextView) rootView.findViewById(R.id.tv_cigarettesSmokedToday);
        moneySavedTextView = (TextView) rootView.findViewById(R.id.tv_moneySaved);
        cigarettesNotSmokedTextView = (TextView) rootView.findViewById(R.id.tv_cigarettesNotSmokedToday);

        ProfileManager profileManager = new ProfileManager(getContext());
        profile = profileManager.getCurrentProfile();

        if (profile == null || profile.getFirstName() == null) {
            return rootView;
        }

        TextView welcome = (TextView) rootView.findViewById(R.id.welcomeTextView);
        welcome.append(profile.getFirstName());

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

                    String notSmokedFor = tileData.getString("notSmokedFor");
                    notSmokedFor = notSmokedFor.equals("No data found!") ? "Geen Data" : notSmokedFor;

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
        TextView TVQuote = (TextView) rootView.findViewById(R.id.motivationText);
        TVQuote.setText(getRandomQuote());

        fillTiles();
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
        Random random = new Random();
        int randomNumber = random.nextInt(quoteList.size());
        return quoteList.get(randomNumber);
    }


}