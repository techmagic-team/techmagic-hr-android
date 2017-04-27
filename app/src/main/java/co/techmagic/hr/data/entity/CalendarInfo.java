package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by techmagic on 4/27/17.
 */

public class CalendarInfo {

    @SerializedName("_id")
    private String id;

    @SerializedName("dateFrom")
    private String dateFrom;

    @SerializedName("dateTo")
    private String dateTo;

    @SerializedName("_user")
    private String userId;

    @SerializedName("_company")
    private String companyId;

    @SerializedName("__v")
    private String v;

    @SerializedName("isPaid")
    private boolean isPaid;

    @SerializedName("isAccepted")
    private boolean isAccepted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
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

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
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
}
