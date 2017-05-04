package co.techmagic.hr.presentation.ui.adapter.calendar;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;

public interface IGuideXItem {

    void setCalendarInfo(List<CalendarInfo> calendarInfos);

    List<CalendarInfo> getCalendarInfo();
}
