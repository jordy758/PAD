package hva.groepje12.quitsmokinghabits.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Profile {
    private String firstName;
    private String lastName;
    private Calendar birthDate;
    private Gender gender;
    private String notificationToken;
    private List<String> alarms;
    private List<String> games;

    public Profile() {
        alarms = new ArrayList<>();
    }

    public Profile(String firstName, String lastName, Calendar birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;

        alarms = new ArrayList<>();
    }

    public List<String> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<String> alarms) {
        this.alarms = alarms;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNotificationToken() {
        return this.notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public Calendar getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<String> getGames() {
        return this.games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public enum Gender {male, female}
}
