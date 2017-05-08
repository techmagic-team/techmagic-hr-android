package co.techmagic.hr.presentation.ui.adapter.calendar;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.RequestedTimeOff;

public interface IGuideXItem {

    void setCalendarInfo(List<CalendarInfo> calendarInfos);

    List<CalendarInfo> getCalendarInfo();

    void setDayOffs(List<RequestedTimeOff> dayOffs);

    List<RequestedTimeOff> getDayOffs();

    void setVacations(List<RequestedTimeOff> vacations);

    List<RequestedTimeOff> getVacations();

    void setIllnesses(List<RequestedTimeOff> illnesses);

    List<RequestedTimeOff> getIllnesses();

    void setRequested(List<RequestedTimeOff> requested);

    List<RequestedTimeOff> getRequested();
}
