package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class DateFrom {

    @SerializedName("$gte")
    private long gte;

    public DateFrom(long gte) {
        this.gte = gte;
    }

    public long getGte() {
        return gte;
    }
}
