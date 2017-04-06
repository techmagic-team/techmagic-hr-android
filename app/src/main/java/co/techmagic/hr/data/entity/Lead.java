package co.techmagic.hr.data.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Lead implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("lastWorkingDay")
    private String lastWorkingDay;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

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

    public Lead(Parcel in) {
        id = in.readString();
        lastWorkingDay = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(lastWorkingDay);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    public static final Parcelable.Creator<Lead> CREATOR = new Parcelable.Creator<Lead>() {
        @Override
        public Lead createFromParcel(Parcel in) {
            return new Lead(in);
        }

        @Override
        public Lead[] newArray(int size) {
            return new Lead[size];
        }
    };
}