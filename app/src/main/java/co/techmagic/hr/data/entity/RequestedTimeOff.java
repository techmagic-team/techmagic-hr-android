package co.techmagic.hr.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RequestedTimeOff implements Parcelable {

    @SerializedName("_user")
    private String userId;

    @SerializedName("_company")
    private String companyId;

    @SerializedName("dateFrom")
    private Date dateFrom;

    @SerializedName("dateTo")
    private Date dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

    @SerializedName("isAccepted")
    private boolean isAccepted;


    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public boolean isAccepted() {
        return isAccepted;
    }


    protected RequestedTimeOff(Parcel in) {
        userId = in.readString();
        companyId = in.readString();
        long tmpDateFrom = in.readLong();
        dateFrom = tmpDateFrom != -1 ? new Date(tmpDateFrom) : null;
        long tmpDateTo = in.readLong();
        dateTo = tmpDateTo != -1 ? new Date(tmpDateTo) : null;
        isPaid = in.readByte() != 0x00;
        isAccepted = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(companyId);
        dest.writeLong(dateFrom != null ? dateFrom.getTime() : -1L);
        dest.writeLong(dateTo != null ? dateTo.getTime() : -1L);
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