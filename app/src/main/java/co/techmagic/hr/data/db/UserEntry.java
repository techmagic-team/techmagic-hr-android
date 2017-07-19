package co.techmagic.hr.data.db;

import co.techmagic.hr.data.entity.UserProfile;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserEntry extends RealmObject {

    @PrimaryKey
    private String id;
    private DepartmentEntry department;
    private RoomEntry room;
    private LeadEntry lead;
    private ReasonEntry reason;
    private EmergencyEntry emergency;
    private String company;
    private String birthday;
    private String description;
    private String email;
    private String firstName;
    private String lastName;
    private String firstWorkingDay;
    private String generalFirstWorkingDay;
    private String lastWorkingDay;
    private int gender;
    private String phone;
    private String photo;
    private String photoOrigin;
    private String relocationCity;
    private int role;
    private String skype;
    private boolean isActive;
    private String trialPeriodEnds;
    private String pdpLink;
    private String oneToOneLink;
    private String reasonComments;

    public UserEntry(final UserProfile userProfile) {
        if (userProfile == null) {
            return;
        }

        id = userProfile.getId();
        department = new DepartmentEntry(userProfile.getDepartment());
        room = new RoomEntry(userProfile.getRoom());
        lead = new LeadEntry(userProfile.getLead());
        reason = new ReasonEntry(userProfile.getReason());
        emergency = new EmergencyEntry(userProfile.getEmergencyContact());
        company = userProfile.getCompany();
        birthday = userProfile.getBirthday();
        description = userProfile.getDescription();
        email = userProfile.getEmail();
        firstName = userProfile.getFirstName();
        lastName = userProfile.getLastName();
        firstWorkingDay = userProfile.getFirstWorkingDay();
        generalFirstWorkingDay = userProfile.getGeneralFirstWorkingDay();
        lastWorkingDay = userProfile.getLastWorkingDay();
        gender = userProfile.getGender();
        phone = userProfile.getPhone();
        photo = userProfile.getPhoto();
        photoOrigin = userProfile.getPhotoOrigin();
        relocationCity = userProfile.getRelocationCity();
        role = userProfile.getRole();
        skype = userProfile.getSkype();
        isActive = userProfile.isActive();
        trialPeriodEnds = userProfile.getTrialPeriodEnds();
        pdpLink = userProfile.getPdpLink();
        oneToOneLink = userProfile.getOneToOneLink();
        reasonComments = userProfile.getReasonComments();
    }

    public String getId() {
        return id;
    }

    public DepartmentEntry getDepartment() {
        return department;
    }

    public RoomEntry getRoom() {
        return room;
    }

    public LeadEntry getLead() {
        return lead;
    }

    public ReasonEntry getReason() {
        return reason;
    }

    public EmergencyEntry getEmergency() {
        return emergency;
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