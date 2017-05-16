package co.techmagic.hr.presentation.pojo;

import java.util.Date;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.presentation.ui.view.calendar.TimeRange;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class UserTimeOff {

    private String userId;
    private String companyId;
    private Date dateFrom;
    private Date dateTo;
    private boolean isPaid;
    private boolean isAccepted;
    private TimeOffType timeOffType;
    private TimeRange timeRange;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public TimeOffType getTimeOffType() {
        return timeOffType;
    }

    public void setTimeOffType(TimeOffType timeOffType) {
        this.timeOffType = timeOffType;
    }

    public TimeRange getTimeRange() {
        timeRange = new TimeRange(dateFrom, dateTo);
        return timeRange;
    }
}
