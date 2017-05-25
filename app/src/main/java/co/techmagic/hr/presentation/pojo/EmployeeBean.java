package co.techmagic.hr.presentation.pojo;

import com.google.gson.annotations.SerializedName;

import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Room;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class EmployeeBean {

    private Department department;
    private Room room;
    private Lead lead;
    private String id;
    private String company;
    private String birthday;
    private String description;
    private String email;
    private EmergencyContact emergencyContact;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("firstWorkingDay")
    private String firstWorkingDay;

    @SerializedName("generalFirstWorkingDay")
    private String generalFirstWorkingDay;

    @SerializedName("lastWorkingDay")
    private String lastWorkingDay;

    @SerializedName("gender")
    private int gender;

    @SerializedName("phone")
    private String phone;

    @SerializedName("photo")
    private String photo;

    @SerializedName("photoOrigin")
    private String photoOrigin;

    @SerializedName("relocationCity")
    private String relocationCity;

    @SerializedName("role")
    private int role;

    @SerializedName("skype")
    private String skype;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("trialPeriodEnds")
    private String trialPeriodEnds;
}
