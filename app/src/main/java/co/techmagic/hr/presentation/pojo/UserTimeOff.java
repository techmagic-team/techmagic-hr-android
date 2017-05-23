package co.techmagic.hr.presentation.pojo;

import java.util.Calendar;
import java.util.Date;

import co.techmagic.hr.common.TimeOffType;
import co.techmagic.hr.presentation.ui.view.calendar.TimeRange;
import co.techmagic.hr.presentation.util.DateUtil;

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

    private Calendar from;
    private Calendar to;

    public UserTimeOff() {
        from = Calendar.getInstance();
        to = Calendar.getInstance();
    }

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

    public Calendar getDateFrom() {
        return from;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        from.setTime(dateFrom);
        from.setTimeInMillis(DateUtil.calendarToMidnightMillis(from));
    }

    public Calendar getDateTo() {
        return to;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        to.setTime(dateTo);
        to.set(to.get(Calendar.YEAR), to.get(Calendar.MONTH), to.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
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
        return new TimeRange(dateFrom, dateTo);
    }
}