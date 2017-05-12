package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RequestedTimeOff implements Parcelable {

    @SerializedName("_user")
    private String userId;

    @SerializedName("_company")
    private String companyId;

    @SerializedName("dateFrom")
    private String dateFrom;

    @SerializedName("dateTo")
    private String dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

    @SerializedName("isAccepted")
    private boolean isAccepted;


    protected RequestedTimeOff(Parcel in) {
        userId = in.readString();
        companyId = in.readString();
        dateFrom = in.readString();
        dateTo = in.readString();
        isPaid = in.readByte() != 0x00;
        isAccepted = in.readByte() != 0x00;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(companyId);
        dest.writeString(dateFrom);
        dest.writeString(dateTo);
        dest.writeByte((byte) (isPaid ? 0x01 : 0x00));
        dest.writeByte((byte) (isAccepted ? 0x01 : 0x00));
    }

    public static final Parcelable.Creator<RequestedTimeOff> CREATOR = new Parcelable.Creator<RequestedTimeOff>() {
        @Override
        public RequestedTimeOff createFromParcel(Parcel in) {
            return new RequestedTimeOff(in);
        }

        @Override
        public RequestedTimeOff[] newArray(int size) {
            return new RequestedTimeOff[size];
        }
    };
}