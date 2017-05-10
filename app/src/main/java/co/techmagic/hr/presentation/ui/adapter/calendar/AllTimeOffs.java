package co.techmagic.hr.presentation.ui.adapter.calendar;

import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.RequestedTimeOff;

/**
 * Holds all time offs which are ready to display at calendar.
 **/

public class AllTimeOffs implements IGuideXItem {

    private List<CalendarInfo> info;
    private List<RequestedTimeOff> dayOffs;
    private List<RequestedTimeOff> vacations;
    private List<RequestedTimeOff> illnesses;
    private List<RequestedTimeOff> requested;


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


    @Override
    public void setDayOffs(List<RequestedTimeOff> dayOffs) {
        this.dayOffs = dayOffs;
    }


    @Override
    public List<RequestedTimeOff> getDayOffs() {
        return dayOffs;
    }


    @Override
    public boolean hasDayOffs() {
        return dayOffs != null && !dayOffs.isEmpty();
    }


    @Override
    public void setVacations(List<RequestedTimeOff> vacations) {
        this.vacations = vacations;
    }


    @Override
    public List<RequestedTimeOff> getVacations() {
        return vacations;
    }


    @Override
    public boolean hasVacation() {
        return vacations != null && !vacations.isEmpty();
    }


    @Override
    public void setIllnesses(List<RequestedTimeOff> illnesses) {
        this.illnesses = illnesses;
    }


    @Override
    public List<RequestedTimeOff> getIllnesses() {
        return illnesses;
    }

    @Override
    public boolean hasIllnesses() {
        return illnesses != null && !illnesses.isEmpty();
    }


    @Override
    public void setRequested(List<RequestedTimeOff> requested) {
        this.requested = requested;
    }


    @Override
    public List<RequestedTimeOff> getRequested() {
        return requested;
    }


    @Override
    public boolean hasRequested() {
        return requested != null && !requested.isEmpty();
    }
}