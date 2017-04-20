package hva.groepje12.quitsmokinghabits.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hva.groepje12.quitsmokinghabits.model.Alarm;
import hva.groepje12.quitsmokinghabits.model.Notification;
import hva.groepje12.quitsmokinghabits.model.Profile;
import hva.groepje12.quitsmokinghabits.util.ProfileManager;
import hva.groepje12.quitsmokinghabits.R;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText birthDate, firstName, lastName;
    Button mEmailSignInButton, sendNotificationButton, sendTimedNotificationButton;

    Calendar pickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthDate = (EditText) findViewById(R.id.edit_text_birth_date);
        firstName = (EditText) findViewById(R.id.edit_text_first_name);
        lastName = (EditText) findViewById(R.id.edit_text_last_name);

        mEmailSignInButton = (Button) findViewById(R.id.button_create_account);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        ProfileManager profileManager = new ProfileManager(this);
        Profile profile = profileManager.getProfile();

        Toast.makeText(this, "Hoi ik ben " + profile.getFullName(), Toast.LENGTH_LONG).show();

        final Notification notify = new Notification("Klik hier om afgeleid te worden", getApplicationContext());
        final Alarm alarm = new Alarm(00, 00, getApplicationContext());

        final Button button_notify = (Button) findViewById(R.id.button_send_notification);
        button_notify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notify.startNotification();
            }
        });

        final Button button_alarm = (Button) findViewById(R.id.button_send_timed_notification);
        button_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int hour = new Time(System.currentTimeMillis()).getHours();
                int minutes = new Time(System.currentTimeMillis()).getMinutes();

                alarm.setHour(hour);
                alarm.setMinutes(minutes + 1);
                alarm.startAlarm();
            }
        });
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar) {
        pickedTime = calendar;
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        birthDate.setText(dateFormat.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void registerUser() {
        Profile profile = new Profile(firstName.getText().toString(), lastName.getText().toString(),
                pickedTime, Profile.Gender.female);

        Toast.makeText(this, "Profiel opgeslagen!", Toast.LENGTH_SHORT).show();

        //Save the profile with a profile manager into the storage
        ProfileManager profileManager = new ProfileManager(this);
        profileManager.save(profile);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR) - 20;
            int month = 0;
            int day = 1;

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,
                    (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);

            dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            return dpd;
        }
    }

}

