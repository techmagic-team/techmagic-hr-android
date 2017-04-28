package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarInfo {

    @SerializedName("holidays")
    private List<Holiday> holidays;

    @SerializedName("isCurrent")
    private boolean isCurrent;

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public boolean isCurrent() {
        return isCurrent;
    }
}
