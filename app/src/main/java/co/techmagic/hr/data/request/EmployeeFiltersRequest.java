package co.techmagic.hr.data.request;


public class EmployeeFiltersRequest {

    private String query;
    private String departmentId;
    private String leadId;
    private String projectId;
    private int offset;
    private int limit;
    private boolean isLastWorkingDay;

    public EmployeeFiltersRequest(String query, String departmentId, String leadId, String projectId, int offset, int limit, boolean isLastWorkingDay) {
        this.query = query;
        this.departmentId = departmentId;
        this.leadId = leadId;
        this.projectId = projectId;
        this.offset = offset;
        this.limit = limit;
        this.isLastWorkingDay = isLastWorkingDay;
    }

    public String getQuery() {
        return query;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getLeadId() {
        return leadId;
    }

    public String getProjectId() {
        return projectId;
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
