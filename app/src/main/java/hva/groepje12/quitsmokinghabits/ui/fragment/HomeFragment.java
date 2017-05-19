package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;
import hva.groepje12.quitsmokinghabits.util.Utilities;

public class HomeFragment extends Fragment {

    private List<String> games;
    private View rootView;

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

        quoteList.add("0");
        quoteList.add("1");
        quoteList.add("2");
        quoteList.add("3");
        quoteList.add("4");

        ProfileManager profileManager = new ProfileManager(getContext());
        final Profile profile = profileManager.getCurrentProfile();
        TextView welcome = (TextView) rootView.findViewById(R.id.welcomeTextView);

        if (profile != null && profile.getFirstName() != null) {
            welcome.append(profile.getFirstName());
        }
        createChart();

        return rootView;
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