package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

import co.techmagic.hr.common.AcceptedTimeOffType;

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
    private Boolean stringIsAccepted;

    private AcceptedTimeOffType acceptedTimeOffType = AcceptedTimeOffType.NULL;


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

    public Boolean getStringIsAccepted() {
        return stringIsAccepted;
    }

    public AcceptedTimeOffType getAcceptedTimeOffType() {
        /*if (stringIsAccepted.equalsIgnoreCase("true")) {
            acceptedTimeOffType = AcceptedTimeOffType.TRUE;

        } else if (stringIsAccepted.equalsIgnoreCase("false")) {
            acceptedTimeOffType = AcceptedTimeOffType.FALSE;
        }*/

        return acceptedTimeOffType;
    }
}