package hva.groepje12.quitsmokinghabits.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hva.groepje12.quitsmokinghabits.api.OnLoopJEvent;
import hva.groepje12.quitsmokinghabits.api.tasks.RegisterDeviceTask;
import hva.groepje12.quitsmokinghabits.R;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText firstNameEditText, lastNameEditText, birthDateEditText;
    Button createAccountButton;

    Calendar pickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthDateEditText = (EditText) findViewById(R.id.edit_text_birth_date);
        firstNameEditText = (EditText) findViewById(R.id.edit_text_first_name);
        lastNameEditText = (EditText) findViewById(R.id.edit_text_last_name);

        createAccountButton = (Button) findViewById(R.id.button_create_account);
        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        ProfileManager profileManager = new ProfileManager(this);
        Profile profile = profileManager.getCurrentProfile();

        if (profile.getFirstName() != null) {
            firstNameEditText.setText(profile.getFirstName());
            lastNameEditText.setText(profile.getLastName());
            setDate(profile.getBirthDate());
            createAccountButton.setText("Opslaan");
        }
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        pickedTime = calendar;
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        birthDateEditText.setText(dateFormat.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void registerUser() {
        String firstName = this.firstNameEditText.getText().toString();
        String lastName = this.lastNameEditText.getText().toString();
        String birthDate = this.birthDateEditText.getText().toString();

        if (firstName.isEmpty() || firstName.length() > 20) {
            this.firstNameEditText.setError("Voornaam leeg of te lang!");
            return;
        }

        if (lastName.isEmpty() || lastName.length() > 20) {
            this.lastNameEditText.setError("Achternaam leeg of te lang!");
            return;
        }

        if (birthDate.isEmpty()) {
            this.birthDateEditText.setError("Vul een geboortedatum in!");
            return;
        }

        ProfileManager profileManager = new ProfileManager(this);
        Profile profile = profileManager.getCurrentProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setBirthDate(pickedTime);
        profile.setGender(Profile.Gender.female);

        //Save the profile with a profile manager into the storage
        profileManager.saveToPreferences(profile);


        //Attempt to call api to register the profile
        RequestParams params = profileManager.getParams();

        RegisterDeviceTask registerDeviceTask = new RegisterDeviceTask(new OnLoopJEvent() {
            @Override
            public void taskCompleted(JSONObject results) {
                Toast.makeText(RegisterActivity.this, "Profiel is opgeslagen!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void taskFailed(String results) {
                Toast.makeText(RegisterActivity.this, "Couldn't register profile, try again!", Toast.LENGTH_SHORT).show();
            }
        });

        registerDeviceTask.execute(params);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = Calendar.getInstance().get(Calendar.YEAR) - 20;
            int month = 0;
            int day = 1;

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
                    (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);

            dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return dpd;
        }
    }

}

