package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class FilterLead implements IFilterModel {

    @SerializedName("_id")
    private String id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("_lead")
    private String lead;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getLastWorkingDay() {
        return null;
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
