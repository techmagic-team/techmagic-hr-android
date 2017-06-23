package co.techmagic.hr.data.entity;


import com.google.gson.annotations.SerializedName;

public class Lead implements IFilterModel {

    @SerializedName("_id")
    private String id;

    @SerializedName("lastWorkingDay")
    private String lastWorkingDay;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    public Lead() {}

    public Lead(String id, String lastWorkingDay, String firstName, String lastName) {
        this.id = id;
        this.lastWorkingDay = lastWorkingDay;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }
}