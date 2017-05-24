package hva.groepje12.quitsmokinghabits.model;

import java.util.ArrayList;
import java.util.Calendar;

public class Profile {
    private int id;
    private String firstName;
    private String lastName;
    private Calendar birthDate;
    private Gender gender;
    private int cigarettesPerDay;
    private int cigarettesPerPack;
    private double pricePerPack;
    private double moneySaved;
    private String notificationToken;
    private ArrayList<Alarm> alarms;
    private ArrayList<String> games;
    private ArrayList<Goal> goals;
    private ArrayList<LocationData> locations;

    public Profile() {
        alarms = new ArrayList<>();
        goals = new ArrayList<>();
        games = new ArrayList<>();
        locations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneySaved() {
        return moneySaved;
    }

    public String getFormattedMoneySaved() {
        return Format.formatDoubleToPrice(moneySaved);
    }

    public void setMoneySaved(double moneySaved) {
        this.moneySaved = moneySaved;
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

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
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

    public ArrayList<LocationData> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationData> locations) {
        this.locations = locations;
    }

    public enum Gender {male, female}
}
