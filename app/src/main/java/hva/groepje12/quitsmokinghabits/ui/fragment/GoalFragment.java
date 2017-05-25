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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.Task;
import hva.groepje12.quitsmokinghabits.model.Format;
import hva.groepje12.quitsmokinghabits.model.Goal;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.service.DataHolder;
import hva.groepje12.quitsmokinghabits.ui.activity.MainActivity;
import hva.groepje12.quitsmokinghabits.util.GoalsAdapter;

public class GoalFragment extends Fragment {
    ListView goalsListView;

    private ArrayList<Goal> goalList;
    private GoalsAdapter goalsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goals_fragment_main, container, false);
        View mainView = getActivity().findViewById(R.id.main_activity);

        goalsListView = (ListView) rootView.findViewById(R.id.list_goals);

        final Profile profile = DataHolder.getCurrentProfile(getContext());

        TextView currentSaldoTextView = (TextView) rootView.findViewById(R.id.current_saldo);
        currentSaldoTextView.append(profile.getFormattedMoneySaved());

        goalList = profile.getGoals();
        Collections.sort(goalList);

        double totalSavings = profile.getMoneySaved();
        for (Goal goal : goalList) {
            if (goal.getAchievedAt() != null) {
                totalSavings += goal.getPrice();
            }
        }

        TextView totalSingsTextView = (TextView) rootView.findViewById(R.id.total_savings);
        totalSingsTextView.append(Format.formatDoubleToPrice(totalSavings));

        goalsAdapter = new GoalsAdapter(getContext(), goalList);
        goalsListView.setAdapter(goalsAdapter);

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);

        goalsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Goal goal = goalList.get(position);

                Task removeGoalTask = new Task(new OnLoopJEvent() {
                    @Override
                    public void taskCompleted(JSONObject results) {
                        goalList.remove(goal);
                        goalsAdapter.notifyDataSetChanged();

                        profile.setGoals(goalList);
                        DataHolder.saveProfileToPreferences(getContext(), profile);
                    }

                    @Override
                    public void taskFailed(JSONObject results) {
                        Toast.makeText(getContext(), "Behaalde doelen kunnen niet worden verwijderd!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fatalError(String results) {

                    }
                });

                RequestParams params = new RequestParams();
                params.add("id", Integer.toString(goal.getId()));

                removeGoalTask.execute(Task.REMOVE_GOAL, params);

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
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(45, 0, 45, 0);

                final EditText doelEditText = new EditText(getContext());
                final EditText benodigdePrijsEditText = new EditText(getContext());
                doelEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                doelEditText.setHint("Naam van doel");

                benodigdePrijsEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                benodigdePrijsEditText.setHint("Benodigde prijs");

                layout.addView(doelEditText, params);
                layout.addView(benodigdePrijsEditText, params);

                alertDialogBuilder.setView(layout);

                alertDialogBuilder.setCancelable(false).setPositiveButton("Toevoegen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String doel = doelEditText.getText().toString();
                        final String benodigePrijs = benodigdePrijsEditText.getText().toString();

                        RequestParams params = new RequestParams();
                        params.put("goal", doel);
                        params.add("price", benodigePrijs);

                        Task addGoalTask = new Task(new OnLoopJEvent() {
                            @Override
                            public void taskCompleted(JSONObject results) {
                                try {
                                    JSONObject response = results.getJSONObject("response");

                                    Gson gson = new GsonBuilder().create();
                                    Goal goal = gson.fromJson(
                                            response.getJSONObject("goal").toString(),
                                            Goal.class
                                    );

                                    goalList.add(goal);
                                    Collections.sort(goalList);
                                    goalsAdapter.notifyDataSetChanged();

                                    profile.setGoals(goalList);
                                    DataHolder.saveProfileToPreferences(getContext(), profile);
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

                        addGoalTask.execute(Task.ADD_GOAL, params);
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
}