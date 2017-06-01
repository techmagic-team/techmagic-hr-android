package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("year")
    private String year;

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

    public String getName() {
        return name;
    }

    public int getYear() {
        return Integer.valueOf(year);
    }
}
