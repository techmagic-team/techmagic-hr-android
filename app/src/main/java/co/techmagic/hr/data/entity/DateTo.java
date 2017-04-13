package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class DateTo {

    @SerializedName("$lte")
    private long lte;

    public DateTo(long lte) {
        this.lte = lte;
    }

    public long getLte() {
        return lte;
    }
}
