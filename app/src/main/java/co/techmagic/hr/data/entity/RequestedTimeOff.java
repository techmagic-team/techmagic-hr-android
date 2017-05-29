package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class RequestedTimeOff {

    @SerializedName("_user")
    private String userId;

    @SerializedName("_company")
    private String companyId;

    @SerializedName("dateFrom")
    private String dateFrom;

    @SerializedName("dateTo")
    private String dateTo;

    @SerializedName("isPaid")
    private boolean isPaid;

    @SerializedName("isAccepted")
    private Boolean isAccepted = null;


    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public Boolean isAccepted() {
        return isAccepted != null && isAccepted;
    }
}