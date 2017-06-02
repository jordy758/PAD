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

import com.loopj.android.http.RequestParams;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.LegendModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
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
import hva.groepje12.quitsmokinghabits.service.DataHolder;

public class HomeFragment extends Fragment {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private View rootView;
    private TextView notSmokedForTextView, todaySmokedTextView, moneySavedTextView, cigarettesNotSmokedTextView;
    private ArrayList<String> quoteList = new ArrayList<>();
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

        Profile profile = DataHolder.getCurrentProfile(getContext());

        if (profile == null || profile.getFirstName() == null) {
            return rootView;
        }

        TextView welcome = (TextView) rootView.findViewById(R.id.welcomeTextView);
        welcome.append(profile.getFirstName());

        createChart();

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
//        TextView TVQuote = (TextView) rootView.findViewById(R.id.motivationText);
//        TVQuote.setText(getRandomQuote());

        fillTiles();
    }

    public void createChart() {
        ValueLineChart mCubicValueLineChart = (ValueLineChart) rootView.findViewById(R.id.smokeChart);

        ValueLineSeries mySeries = new ValueLineSeries();
        mySeries.setColor(Color.GREEN);

        mySeries.addPoint(new ValueLinePoint("Jan", 20f));
        mySeries.addPoint(new ValueLinePoint("Feb", 18f));
        mySeries.addPoint(new ValueLinePoint("Mar", 13f));
        mySeries.addPoint(new ValueLinePoint("Apr", 10f));
        mySeries.addPoint(new ValueLinePoint("Mai", 8f));

        ValueLineSeries preferedSeries = new ValueLineSeries();
        preferedSeries.setColor(Color.parseColor("#E91E63"));

        preferedSeries.addPoint(new ValueLinePoint("Jan", 20f));
        preferedSeries.addPoint(new ValueLinePoint("Feb", 15f));
        preferedSeries.addPoint(new ValueLinePoint("Mar", 10f));
        preferedSeries.addPoint(new ValueLinePoint("Apr", 5f));
        preferedSeries.addPoint(new ValueLinePoint("Mai", 0f));

        mCubicValueLineChart.addSeries(mySeries);
        mCubicValueLineChart.addSeries(preferedSeries);
        ArrayList<LegendModel> legendModels = new ArrayList<>();
        legendModels.add(new LegendModel("Jij"));
        legendModels.add(new LegendModel("Perfecte Lijn"));
        mCubicValueLineChart.addLegend(legendModels);
        mCubicValueLineChart.startAnimation();
    }

    public String getRandomQuote() {
        Random random = new Random();
        int randomNumber = random.nextInt(quoteList.size());
        return quoteList.get(randomNumber);
    }


}