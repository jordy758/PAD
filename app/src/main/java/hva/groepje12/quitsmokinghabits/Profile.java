package hva.groepje12.quitsmokinghabits;

import java.util.Calendar;

public class Profile {
    private String firstName;
    private String lastName;
    private Calendar birthDate;
    private Gender gender;

    public enum Gender {male, female};

    public Profile() {};

    public Profile(String firstName, String lastName, Calendar birthDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() { return this.getFirstName() + " " + this.getLastName(); }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public Calendar getBirthDate() {
        return this.birthDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }
}
