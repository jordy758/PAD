package hva.groepje12.quitsmokinghabits.model;

import java.util.ArrayList;
import java.util.Calendar;

public class Profile {
    private String firstName;
    private String lastName;
    private Calendar birthDate;
    private Gender gender;
    private int cigarettesPerDay;
    private int cigarettesPerPack;
    private double pricePerPack;
    private String notificationToken;
    private ArrayList<String> alarms;
    private ArrayList<String> games;
    private ArrayList<Goal> goals;

    public Profile() {
        alarms = new ArrayList<>();
        goals = new ArrayList<>();
    }

    public Profile(String firstName, String lastName, Calendar birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;

        alarms = new ArrayList<>();
        goals = new ArrayList<>();
    }

    public int getCigarettesPerDay() {
        return cigarettesPerDay;
    }

    public void setCigarettesPerDay(int cigarettesPerDay) {
        this.cigarettesPerDay = cigarettesPerDay;
    }

    public int getCigarettesPerPack() {
        return cigarettesPerPack;
    }

    public void setCigarettesPerPack(int cigarettesPerPack) {
        this.cigarettesPerPack = cigarettesPerPack;
    }

    public double getPricePerPack() {
        return pricePerPack;
    }

    public void setPricePerPack(double pricePerPack) {
        this.pricePerPack = pricePerPack;
    }

    public ArrayList<String> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<String> alarms) {
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

    public ArrayList<String> getGames() {
        return this.games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }

    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public enum Gender {male, female}
}
