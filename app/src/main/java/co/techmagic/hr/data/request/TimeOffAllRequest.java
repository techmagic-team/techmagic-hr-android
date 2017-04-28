package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;

public class TimeOffAllRequest {

    @SerializedName("dateFrom")
    private long dateFrom;

    @SerializedName("dateTo")
    private long dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

    public TimeOffAllRequest(long dateFrom, long dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public TimeOffAllRequest(long dateFrom, long dateTo, boolean isPaid) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.isPaid = isPaid;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTo() {
        return dateTo;
    }

    public void setDateTo(long dateTo) {
        this.dateTo = dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
