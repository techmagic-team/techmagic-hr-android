package co.techmagic.hr.data.request;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Reason;
import co.techmagic.hr.data.entity.Room;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.presentation.util.DateUtil;


public class EditProfileRequest {

    @SerializedName("_department")
    private Department department;

    @SerializedName("_room")
    private Room room;

    @SerializedName("_lead")
    private Lead lead;

    @SerializedName("_reason")
    private Reason reason;

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

    @SerializedName("password")
    private String password;

    @SerializedName("emergencyContact")
    private EmergencyContact emergencyContact;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("firstWorkingDay")
    private Long firstWorkingDay;

    @SerializedName("generalFirstWorkingDay")
    private Long generalFirstWorkingDay;

    @SerializedName("lastWorkingDay")
    private Long lastWorkingDay;

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

    public EditProfileRequest(@NonNull UserProfile user) {
        Department dep = user.getDepartment();
        department = dep == null ? null : dep;

        Room r = user.getRoom();
        room = r == null ? null : r;

        Lead l = user.getLead();
        lead = l == null ? null : l;

        Reason rsn = user.getReason();
        reason = rsn == null ? null : rsn;

        id = user.getId();
        company = user.getCompany();
        birthday = user.getBirthday();
        description = user.getDescription();
        email = user.getEmail();
        password = user.getPassword();
        emergencyContact = user.getEmergencyContact();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        firstWorkingDay = DateUtil.getFormattedDateInMillis(user.getFirstWorkingDay());
        generalFirstWorkingDay = DateUtil.getFormattedDateInMillis(user.getGeneralFirstWorkingDay());
        lastWorkingDay = DateUtil.getFormattedDateInMillis(user.getLastWorkingDay());
        gender = user.getGender();
        phone = user.getPhone();
        photo = user.getPhoto();
        photoOrigin = user.getPhotoOrigin();
        relocationCity = user.getRelocationCity();
        role = user.getRole();
        skype = user.getSkype();
        isActive = user.isActive();
        trialPeriodEnds = user.getTrialPeriodEnds();
        pdpLink = user.getPdpLink();
        oneToOneLink = user.getOneToOneLink();
        reasonComments = user.getReasonComments();
        password = user.getPassword();
    }

    public String getId() {
        return id;
    }
}