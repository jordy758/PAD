package hva.groepje12.quitsmokinghabits.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Goal;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;
import hva.groepje12.quitsmokinghabits.util.GoalsAdapter;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

public class GoalFragment extends Fragment {
    ListView gamesList;

    private ArrayList<Goal> goalList;

    private ProfileManager profileManager;
    private Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goals_fragment_main, container, false);
        View mainView = getActivity().findViewById(R.id.main_activity);

        gamesList = (ListView) rootView.findViewById(R.id.list_goals);

        profileManager = new ProfileManager(getActivity());
        profile = profileManager.getCurrentProfile();

        goalList = profile.getGoals();
        final GoalsAdapter goalsAdapter = new GoalsAdapter(getContext(), goalList);
        gamesList.setAdapter(goalsAdapter);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);

        gamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                goalList.remove(position);
                goalsAdapter.notifyDataSetChanged();

                profile.setGoals(goalList);
                profileManager.saveToPreferences(profile);
                return true;
            }
        });

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

                        final Goal goal = new Goal(doel, Double.parseDouble(benodigePrijs));

                        RequestParams params = new RequestParams();
                        params.put("goal", doel);
                        params.add("price", benodigePrijs);
                        params.add("notification_token", profile.getNotificationToken());

                        Task addSmokeDataTask = new Task(new OnLoopJEvent() {
                            @Override
                            public void taskCompleted(JSONObject results) {
                                goalList.add(goal);
                                goalsAdapter.notifyDataSetChanged();

                                profile.setGoals(goalList);
                                profileManager.saveToPreferences(profile);
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