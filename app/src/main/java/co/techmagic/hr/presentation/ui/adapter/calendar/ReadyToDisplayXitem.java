package co.techmagic.hr.presentation.ui.adapter.calendar;

import android.support.annotation.Nullable;

import java.util.List;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.presentation.ui.view.calendar.TimeRange;

public class ReadyToDisplayXitem implements IGridItem {

    private List<Docs> employees;
    private TimeRange timeRange;
    private String personName;
    private String photoUrl;


    @Override
    public void setEmployees(List<Docs> employees) {
        this.employees = employees;
    }


    @Override
    public List<Docs> getEmployees() {
        return employees;
    }


    @Override
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }


    @Nullable
    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }


    @Override
    public void setPersonName(String personName) {
        this.personName = personName;
    }


    @Override
    public String getPersonName() {
        return personName;
    }


    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }
}