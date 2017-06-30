package co.techmagic.hr.domain.pojo;

import java.util.Date;

import co.techmagic.hr.common.TimeOffType;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class RequestedTimeOffDto {

    private String id;
    private String userId;
    private String companyId;
    private Date dateFrom;
    private Date dateTo;
    private boolean isPaid;
    private Boolean isAccepted = null;
    private TimeOffType timeOffType;

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

    public Boolean getAccepted() {
        return isAccepted;
    }

    public Boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public TimeOffType getTimeOffType() {
        return timeOffType;
    }

    public void setTimeOffType(TimeOffType timeOffType) {
        this.timeOffType = timeOffType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
