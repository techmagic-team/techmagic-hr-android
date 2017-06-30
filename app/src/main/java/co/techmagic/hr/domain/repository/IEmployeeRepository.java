package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffRequestByUser;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.pojo.DatePeriodDto;
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto;
import rx.Observable;

public interface IEmployeeRepository {

    Observable<Employee> getEmployees(EmployeeFiltersRequest request);

    Observable<List<Filter>> getFilterDepartments();

    Observable<List<FilterLead>> getFilterLeadsWithEmployees();

    Observable<List<Filter>> getProjectFilters();

    Observable<List<RequestedTimeOff>> getUserVacations(TimeOffRequest request);

    Observable<List<RequestedTimeOff>> getUserDayOffs(TimeOffRequest request);

    Observable<List<RequestedTimeOff>> getUserIllnesses(GetIllnessRequest request);

    Observable<List<RequestedTimeOff>> getAllVacations(TimeOffAllRequest request);

    Observable<List<RequestedTimeOff>> getAllDayOffs(TimeOffAllRequest request);

    Observable<List<RequestedTimeOff>> getAllIllnesses(TimeOffAllRequest request);

    Observable<List<RequestedTimeOff>> getUsedVacationsByUser(TimeOffRequestByUser request);

    Observable<List<RequestedTimeOff>> getUsedDayOffsByUser(TimeOffRequestByUser request);

    Observable<List<RequestedTimeOff>> getUsedIllnessesByUser(TimeOffRequestByUser request);

    Observable<Employee> getAllEmployeesByDepartment(EmployeesByDepartmentRequest employeesByDepartmentRequest);

    Observable<List<CalendarInfo>> getCalendar(TimeOffAllRequest request);

    Observable<List<Filter>> getRooms();

    Observable<List<FilterLead>> getFilterLeads();

    Observable<List<Filter>> getReasons();

    Observable<RequestedTimeOffDto> requestVacation(TimeOffRequest request);

    Observable<RequestedTimeOffDto> requestIllness(TimeOffRequest request);

    Observable<Integer> getTotalVacation(TimeOffRequestByUser request);

    Observable<Integer> getTotalDayOff(TimeOffRequestByUser request);

    Observable<Integer> getTotalIllness(TimeOffRequestByUser request);

    Observable<List<DatePeriodDto>> getUserPeriods(String userId);

    Observable<Void> deleteVacation(String timeOffId);

    Observable<Void> deleteIllness(String timeOffId);
}
