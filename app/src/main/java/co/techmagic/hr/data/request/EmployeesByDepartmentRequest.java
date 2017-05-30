package co.techmagic.hr.data.request;

/**
 * Created by techmagic on 4/27/17.
 */

public class EmployeesByDepartmentRequest {

    private String projectId;
    private String departmentId;
    private boolean isMyTeam;

    public EmployeesByDepartmentRequest(String projectId, String departmentId, boolean isMyTeam) {
        this.projectId = projectId;
        this.departmentId = departmentId;
        this.isMyTeam = isMyTeam;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public boolean isMyTeam() {
        return isMyTeam;
    }
}
