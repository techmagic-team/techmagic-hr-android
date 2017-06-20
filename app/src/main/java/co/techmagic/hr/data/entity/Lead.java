package co.techmagic.hr.data.entity;


import com.google.gson.annotations.SerializedName;

public class Lead {

    @SerializedName("_id")
    private String id;

    @SerializedName("lastWorkingDay")
    private String lastWorkingDay;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(String lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}