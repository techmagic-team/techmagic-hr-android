package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;

import co.techmagic.hr.data.entity.DateFrom;
import co.techmagic.hr.data.entity.DateTo;

public class TimeOffAllRequest {

    @SerializedName("dateFrom")
    private long dateFrom;

    @SerializedName("dateTo")
    private long dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

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
