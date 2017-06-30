package co.techmagic.hr.data.request;

import android.support.annotation.NonNull;

import co.techmagic.hr.data.entity.Department;
import co.techmagic.hr.data.entity.EmergencyContact;
import co.techmagic.hr.data.entity.Lead;
import co.techmagic.hr.data.entity.Reason;
import co.techmagic.hr.data.entity.Room;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.presentation.util.DateUtil;


public class EditProfileRequest {

    private Department department;
    private Room room;
    private Lead lead;
    private Reason reason;
    private String id;
    private String company;
    private String birthday;
    private String description;
    private String email;
    private String password;
    private EmergencyContact emergencyContact;
    private String firstName;
    private String lastName;
    private Long firstWorkingDay = null;
    private Long generalFirstWorkingDay = null;
    private Long lastWorkingDay = null;
    private int gender;
    private String phone;
    private String photo;
    private String photoOrigin;
    private String relocationCity;
    private int role;
    private String skype;
    private boolean isActive;
    private String trialPeriodEnds = null;
    private String pdpLink;
    private String oneToOneLink;
    private String reasonComments;

    public EditProfileRequest(@NonNull UserProfile user) {
        department = user.getDepartment();
        room = user.getRoom();
        lead = user.getLead();
        reason = user.getReason();
        id = user.getId();
        company = user.getCompany();
        birthday = user.getBirthday();
        description = user.getDescription();
        email = user.getEmail();
        password = user.getPassword();
        emergencyContact = user.getEmergencyContact();
        firstName = user.getFirstName();
        lastName = user.getLastName();
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
        firstWorkingDay = DateUtil.getFormattedDateInMillis(user.getFirstWorkingDay());
        generalFirstWorkingDay = DateUtil.getFormattedDateInMillis(user.getGeneralFirstWorkingDay());
        lastWorkingDay = DateUtil.getFormattedDateInMillis(user.getLastWorkingDay());
    }

    public String getId() {
        return id;
    }

    public Department getDepartment() {
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

    public String getPassword() {
        return password;
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

    public Long getFirstWorkingDay() {
        return firstWorkingDay;
    }

    public Long getGeneralFirstWorkingDay() {
        return generalFirstWorkingDay;
    }

    public Long getLastWorkingDay() {
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