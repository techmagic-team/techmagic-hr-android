package co.techmagic.hr.presentation.pojo;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    private Boolean isAccepted = null;
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

    /**
     * Sets time in UTC timezone
     */

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        from.setTime(dateFrom);
        from.setTimeZone(TimeZone.getTimeZone("UTC"));
        from.setTimeInMillis(DateUtil.calendarToMidnightMillis(from)); /* !!! Should be set to midnight !!! */
    }

    public Calendar getDateTo() {
        return to;
    }

    /**
     * Sets time in UTC timezone
     */

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        to.setTime(dateTo);
        to.setTimeZone(TimeZone.getTimeZone("UTC"));
        to.setTimeInMillis(DateUtil.calendarToEndOfTheDayMillis(to)); /* !!! Should be set at the end of the day !!! */
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Boolean getAccepted() {
        Log.e("Boolean value", String.valueOf(isAccepted));
        if (isAccepted == null) {
            Log.e("get Boolean null", "from: " + DateUtil.getFormattedMonthAndYear(dateFrom) + " to: " + DateUtil.getFormattedMonthAndYear(dateTo));
        }
        return isAccepted;
    }

    public Boolean isAccepted() {
        return isAccepted != null && isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
        if (isAccepted == null) {
            Log.e("set Boolean null", "from: " + DateUtil.getFormattedMonthAndYear(dateFrom) + " to: " + DateUtil.getFormattedMonthAndYear(dateTo));
        }
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