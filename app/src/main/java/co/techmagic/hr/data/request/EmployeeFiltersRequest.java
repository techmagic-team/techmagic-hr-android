package co.techmagic.hr.data.request;


public class EmployeeFiltersRequest {

    private String departmentId;
    private String leadId;
    private int offset;
    private int limit;
    private boolean isLastWorkingDay;

    public EmployeeFiltersRequest(String departmentId, String leadId, int offset, int limit, boolean isLastWorkingDay) {
        this.departmentId = departmentId;
        this.leadId = leadId;
        this.offset = offset;
        this.limit = limit;
        this.isLastWorkingDay = isLastWorkingDay;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getLeadId() {
        return leadId;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isLastWorkingDay() {
        return isLastWorkingDay;
    }
}
