package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import rx.Observable;

public interface IEmployeeRepository {

    Observable<Employee> getEmployees(EmployeeFiltersRequest request);

    Observable<List<FilterDepartment>> getFilterDepartments();

    Observable<List<FilterLead>> getFilterLeads();

    Observable<List<RequestedTimeOff>> getUserVacations(TimeOffRequest request);

    Observable<List<RequestedTimeOff>> getUserIllnesses(GetIllnessRequest request);

    Observable<List<CalendarInfo>> getAllVacations(TimeOffAllRequest request);

    Observable<List<CalendarInfo>> getAllDayOffs(TimeOffAllRequest request);

    Observable<List<CalendarInfo>> getAllIllnesses(TimeOffAllRequest request);

    Observable<List<Employee>> getAllEmployeesByDepartment(EmployeesByDepartmentRequest employeesByDepartmentRequest);

    Observable<List<CalendarInfo>> getCalendar(TimeOffAllRequest request);
}
