package co.techmagic.hr.data.request;

import co.techmagic.hr.data.entity.DateFrom;
import co.techmagic.hr.data.entity.DateTo;

/**
 * Is used for Vacation and Day-off
 * */

public class TimeOffRequest {

    private String userId;
    private boolean isPaid;
    private DateFrom dateFrom;
    private DateTo dateTo;


    public TimeOffRequest(String userId, boolean isPaid, long dateFrom, long dateTo) {
        this.userId = userId;
        this.isPaid = isPaid;
        this.dateFrom = new DateFrom(dateFrom);
        this.dateTo = new DateTo(dateTo);
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public DateFrom getDateFrom() {
        return dateFrom;
    }

    public DateTo getDateTo() {
        return dateTo;
    }


}
