package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Employee implements Parcelable {

    @SerializedName("docs")
    private List<Docs> docs;

    @SerializedName("count")
    private int count;

    public List<Docs> getDocs() {
        return docs;
    }

    public int getCount() {
        return count;
    }

    public Employee(Parcel in) {
        if (in.readByte() == 0x01) {
            docs = new ArrayList<>();
            in.readList(docs, Docs.class.getClassLoader());
        } else {
            docs = null;
        }
        count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (docs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(docs);
        }
        dest.writeInt(count);
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
