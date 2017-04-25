package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;
import co.techmagic.hr.data.entity.DateFrom;
import co.techmagic.hr.data.entity.DateTo;

/**
 * Is used for Vacation and Day-off
 * */

public class TimeOffRequest {

    @SerializedName("_user")
    private String userId;

    @SerializedName("dateFrom")
    private DateFrom dateFrom;

    @SerializedName("dateTo")
    private DateTo dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

    public TimeOffRequest(String userId, long dateFrom, long dateTo, boolean isPaid) {
        this.userId = userId;
        this.dateFrom = new DateFrom(dateFrom);
        this.dateTo = new DateTo(dateTo);
        this.isPaid = isPaid;
    }

    public String getUserId() {
        return userId;
    }

    public DateFrom getDateFrom() {
        return dateFrom;
    }

    public DateTo getDateTo() {
        return dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
