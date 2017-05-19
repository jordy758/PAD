package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Game;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;
import hva.groepje12.quitsmokinghabits.ui.activity.SelectAppActivity;
import hva.groepje12.quitsmokinghabits.util.GameInfoAdapter;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class GoalFragment extends Fragment {

    private ListView goals;
    private ArrayList<ImageView> goalsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.goals_fragment_main, container, false);
        Log.e("DOEL:", "oncreate");
        goalsList = new ArrayList<ImageView>();
        goals = (ListView) rootView.findViewById(R.id.list_goals);

        View mainView = getActivity().findViewById(R.id.main_activity);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.getView().equals(MainActivity.GOALS_VIEW)) {
                    Log.e("TEST", "Goal:" + MainActivity.getView());
                    return;
                }
                Log.e("TEST", "Passed" + MainActivity.getView());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                alertDialogBuilder.setTitle("Nieuw Doel");
                alertDialogBuilder.setMessage("Maak een nieuw doel aan, zodat je kan zien wat je kan doen" +
                        " met de kosten die jij bespaard hebt!.");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(45, 0, 45, 0);

                final EditText doelEditText = new EditText(getContext());
                final EditText benodigdePrijsEditText = new EditText(getContext());
                doelEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                doelEditText.setHint("Naam van doel");

                benodigdePrijsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                benodigdePrijsEditText.setHint("Benodigde prijs");

                layout.addView(doelEditText, params);
                layout.addView(benodigdePrijsEditText, params);

                alertDialogBuilder.setView(layout);

                alertDialogBuilder.setCancelable(false).setPositiveButton("Toevoegen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String doel = doelEditText.getText().toString();
                        String benodigePrijs = benodigdePrijsEditText.getText().toString();

                        ProfileManager profileManager = new ProfileManager(getContext());
                        Profile profile = profileManager.getCurrentProfile();

                        RequestParams params = new RequestParams();
                        params.put("goal", doel);
                        params.add("price", benodigePrijs);
                        params.add("notification_token", profile.getNotificationToken());

                        Task addSmokeDataTask = new Task(new OnLoopJEvent() {
                            @Override
                            public void taskCompleted(JSONObject results) {
                                //Add goal to list
                                
                            }

                            @Override
                            public void taskFailed(JSONObject results) {
                            }

                            @Override
                            public void fatalError(String results) {
                            }
                        });

                        addSmokeDataTask.execute(Task.ADD_GOAL, params);
                    }
                });

                alertDialogBuilder.setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // make keyboard automatically show
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                alertDialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("DOEL:", "onresume");

    }
}