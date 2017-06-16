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

    private Department _department;
    private Room _room;
    private Lead _lead;
    private Reason _reason;
    private String _id;
    private String _company;
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
    private String _pdp;
    private String _oneToOne;
    private String reason_comments;

    public EditProfileRequest(@NonNull UserProfile user) {
        Department dep = user.getDepartment();
        _department = dep == null ? null : dep;

        Room r = user.getRoom();
        _room = r == null ? null : r;

        Lead l = user.getLead();
        _lead = l == null ? null : l;

        Reason rsn = user.getReason();
        _reason = rsn == null ? null : rsn;

        _id = user.getId();
        _company = user.getCompany();
        birthday = user.getBirthday();
        description = user.getDescription();
        email = user.getEmail();
        password = user.getPassword();
        emergencyContact = user.getEmergencyContact();
        firstName = user.getFirstName();
        lastName = user.getLastName();

        if (user.getFirstWorkingDay() == null) {
            firstWorkingDay = null;
        } else {
            firstWorkingDay = DateUtil.getFormattedDateInMillis(user.getFirstWorkingDay());
        }

        if (user.getGeneralFirstWorkingDay() == null) {
            generalFirstWorkingDay = null;
        } else {
            generalFirstWorkingDay = DateUtil.getFormattedDateInMillis(user.getGeneralFirstWorkingDay());
        }

        if (user.getLastWorkingDay() == null) {
            lastWorkingDay = null;
        } else {
            lastWorkingDay = DateUtil.getFormattedDateInMillis(user.getLastWorkingDay());
        }

        gender = user.getGender();
        phone = user.getPhone();
        photo = user.getPhoto();
        photoOrigin = user.getPhotoOrigin();
        relocationCity = user.getRelocationCity();
        role = user.getRole();
        skype = user.getSkype();
        isActive = user.isActive();
        trialPeriodEnds = user.getTrialPeriodEnds();
        _pdp = user.getPdpLink();
        _oneToOne = user.getOneToOneLink();
        reason_comments = user.getReasonComments();
        password = user.getPassword();
    }

    public String getId() {
        return _id;
    }
}