package co.techmagic.hr.data.request;

/**
 * Created by techmagic on 4/27/17.
 */

public class EmployeesByDepartmentRequest {

    private boolean isMyTeam;

    private String departmentId;

    public EmployeesByDepartmentRequest(boolean isMyTeam, String departmentId) {
        this.isMyTeam = isMyTeam;
        this.departmentId = departmentId;
    }

    public boolean isMyTeam() {
        return isMyTeam;
    }

    public void setMyTeam(boolean myTeam) {
        isMyTeam = myTeam;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
