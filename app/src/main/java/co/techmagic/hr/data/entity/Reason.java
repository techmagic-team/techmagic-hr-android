package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Reason implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public Reason() {}

    public Reason(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static final Creator<Reason> CREATOR = new Creator<Reason>() {
        @Override
        public Reason createFromParcel(Parcel in) {
            return new Reason(in);
        }

        @Override
        public Reason[] newArray(int size) {
            return new Reason[size];
        }
    };

    @Override
    public String toString() {
        return "{" +
                "id=\"" + id + '\"' +
                ", name=\"" + name + '\"' +
                '}';
    }
}