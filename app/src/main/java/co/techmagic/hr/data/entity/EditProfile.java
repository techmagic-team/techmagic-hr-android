package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class EditProfile {

    /*@SerializedName("_department")
    private Department department;

    @SerializedName("_room")
    private Room room;

    @SerializedName("_lead")
    private Lead lead;

    @SerializedName("_reason")
    private Reason reason;*/

    @SerializedName("_id")
    private String id;

    @SerializedName("_company")
    private String company;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("description")
    private String description;

    @SerializedName("email")
    private String email;

    @SerializedName("emergencyContact")
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

    @SerializedName("_pdp")
    private String pdpLink;

    @SerializedName("_oneToOne")
    private String oneToOneLink;

    @SerializedName("reason_comments")
    private String reasonComments;

    /*public Department getDepartment() {
        return department;
    }

    public Room getRoom() {
        return room;
    }

    public Lead getLead() {
        return lead;
    }

    public Reason getReason() {
        return reason;
    }*/

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstWorkingDay() {
        return firstWorkingDay;
    }

    public String getGeneralFirstWorkingDay() {
        return generalFirstWorkingDay;
    }

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public int getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhotoOrigin() {
        return photoOrigin;
    }

    public String getRelocationCity() {
        return relocationCity;
    }

    public int getRole() {
        return role;
    }

    public String getSkype() {
        return skype;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getTrialPeriodEnds() {
        return trialPeriodEnds;
    }

    public String getPdpLink() {
        return pdpLink;
    }

    public String getOneToOneLink() {
        return oneToOneLink;
    }

    public String getReasonComments() {
        return reasonComments;
    }
}