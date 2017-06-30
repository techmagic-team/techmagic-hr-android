package co.techmagic.hr.domain.pojo;

import java.util.List;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class CalendarInfoDto {

    private String name;
    private int year;
    private List<HolidayDto> holidays;
    private boolean isCurrent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<HolidayDto> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<HolidayDto> holidays) {
        this.holidays = holidays;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
