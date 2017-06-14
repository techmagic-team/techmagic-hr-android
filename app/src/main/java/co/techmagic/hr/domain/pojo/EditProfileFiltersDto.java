package co.techmagic.hr.domain.pojo;


import java.util.ArrayList;
import java.util.List;

import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;

public class EditProfileFiltersDto {

    private List<Filter> departments = new ArrayList<>();
    private List<FilterLead> leads = new ArrayList<>();
    private List<Filter> rooms = new ArrayList<>();
    private List<Filter> reasons = new ArrayList<>();

    public List<Filter> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Filter> departments) {
        this.departments = departments;
    }

    public List<FilterLead> getLeads() {
        return leads;
    }

    public void setLeads(List<FilterLead> leads) {
        this.leads = leads;
    }

    public List<Filter> getRooms() {
        return rooms;
    }

    public void setRooms(List<Filter> rooms) {
        this.rooms = rooms;
    }

    public List<Filter> getReasons() {
        return reasons;
    }

    public void setReasons(List<Filter> reasons) {
        this.reasons = reasons;
    }
}
