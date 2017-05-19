package co.techmagic.hr.data.request;

public class TimeOffAllRequest {

    private long dateFrom;
    private long dateTo;

    public TimeOffAllRequest(long dateFrom, long dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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
}
