package co.techmagic.hr.presentation.ui.adapter.calendar;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.RequestedTimeOff;

public interface IGuideXItem {

    void setCalendarInfo(List<CalendarInfo> calendarInfos);

    List<CalendarInfo> getCalendarInfo();

    void setDayOffs(List<RequestedTimeOff> dayOffs);

    List<RequestedTimeOff> getDayOffs();

    boolean hasDayOffs();

    void setVacations(List<RequestedTimeOff> vacations);

    List<RequestedTimeOff> getVacations();

    boolean hasVacation();

    void setIllnesses(List<RequestedTimeOff> illnesses);

    List<RequestedTimeOff> getIllnesses();

    boolean hasIllnesses();

    void setRequested(List<RequestedTimeOff> requested);

    List<RequestedTimeOff> getRequested();

    boolean hasRequested();
}