package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;
import co.techmagic.hr.data.entity.DateFrom;
import co.techmagic.hr.data.entity.DateTo;

public class GetIllnessRequest {

    @SerializedName("_user")
    private String userId;

    @SerializedName("requestTimeOffDateFrom")
    private DateFrom dateFrom;

    @SerializedName("requestTimeOffDateTo")
    private DateTo dateTo;

    public GetIllnessRequest(String userId, long dateFrom, long dateTo) {
        this.userId = userId;
        this.dateFrom = new DateFrom(dateFrom);
        this.dateTo = new DateTo(dateTo);
    }

    public String getUserId() {
        return userId;
    }

    public DateFrom getDateFrom() {
        return dateFrom;
    }

    public DateTo getDateTo() {
        return dateTo;
    }
}
