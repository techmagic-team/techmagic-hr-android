package co.techmagic.hr.data.request;


import com.google.gson.annotations.SerializedName;

public class LeadRequest {

    @SerializedName("_id")
    private String id;

    @SerializedName("lastWorkingDay")
    private String lastWorkingDay;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    public LeadRequest(String id, String lastWorkingDay, String firstName, String lastName) {
        this.id = id;
        this.lastWorkingDay = lastWorkingDay;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", lastWorkingDay='" + lastWorkingDay + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName;
    }

    public String getId() {
        return id;
    }

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}