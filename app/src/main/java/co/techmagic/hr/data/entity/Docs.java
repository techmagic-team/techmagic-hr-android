package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Docs implements Parcelable {

    @SerializedName("_department")
    private Department department;

    @SerializedName("_room")
    private Room room;

    @SerializedName("_lead")
    private Lead lead;

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

    public Docs(Parcel in) {
        department = (Department) in.readValue(Department.class.getClassLoader());
        room = (Room) in.readValue(Room.class.getClassLoader());
        lead = (Lead) in.readValue(Lead.class.getClassLoader());
        id = in.readString();
        company = in.readString();
        birthday = in.readString();
        description = in.readString();
        email = in.readString();
        emergencyContact = (EmergencyContact) in.readValue(EmergencyContact.class.getClassLoader());
        firstName = in.readString();
        lastName = in.readString();
        firstWorkingDay = in.readString();
        generalFirstWorkingDay = in.readString();
        lastWorkingDay = in.readString();
        gender = in.readInt();
        phone = in.readString();
        photo = in.readString();
        photoOrigin = in.readString();
        relocationCity = in.readString();
        role = in.readInt();
        skype = in.readString();
        isActive = in.readByte() != 0x00;
        trialPeriodEnds = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(department);
        dest.writeValue(room);
        dest.writeValue(lead);
        dest.writeString(id);
        dest.writeString(company);
        dest.writeString(birthday);
        dest.writeString(description);
        dest.writeString(email);
        dest.writeValue(emergencyContact);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(firstWorkingDay);
        dest.writeString(generalFirstWorkingDay);
        dest.writeString(lastWorkingDay);
        dest.writeInt(gender);
        dest.writeString(phone);
        dest.writeString(photo);
        dest.writeString(photoOrigin);
        dest.writeString(relocationCity);
        dest.writeInt(role);
        dest.writeString(skype);
        dest.writeByte((byte) (isActive ? 0x01 : 0x00));
        dest.writeString(trialPeriodEnds);
    }

    public static final Parcelable.Creator<Docs> CREATOR = new Parcelable.Creator<Docs>() {
        @Override
        public Docs createFromParcel(Parcel in) {
            return new Docs(in);
        }

        @Override
        public Docs[] newArray(int size) {
            return new Docs[size];
        }
    };

    public Department getDepartment() {
        return department;
    }

    public Room getRoom() {
        return room;
    }

    public Lead getLead() {
        return lead;
    }

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
}