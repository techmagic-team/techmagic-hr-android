package co.techmagic.hr.presentation.ui.adapter.calendar;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;

public class AllTimeOffs implements IGuideXItem {

    private List<CalendarInfo> info;


    public AllTimeOffs() {
        info = new ArrayList<>();
    }


    @Override
    public void setCalendarInfo(List<CalendarInfo> calendarInfos) {
        this.info = calendarInfos;
    }


    @Override
    public List<CalendarInfo> getCalendarInfo() {
        return info;
    }
}