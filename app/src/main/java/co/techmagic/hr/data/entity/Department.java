package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Department implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public Department(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Department> CREATOR = new Parcelable.Creator<Department>() {
        @Override
        public Department createFromParcel(Parcel in) {
            return new Department(in);
        }

        @Override
        public Department[] newArray(int size) {
            return new Department[size];
        }
    };
}