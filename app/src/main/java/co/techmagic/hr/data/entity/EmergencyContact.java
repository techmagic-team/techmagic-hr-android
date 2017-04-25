package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EmergencyContact implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public EmergencyContact(Parcel in) {
        name = in.readString();
        phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EmergencyContact> CREATOR = new Parcelable.Creator<EmergencyContact>() {
        @Override
        public EmergencyContact createFromParcel(Parcel in) {
            return new EmergencyContact(in);
        }

        @Override
        public EmergencyContact[] newArray(int size) {
            return new EmergencyContact[size];
        }
    };
}