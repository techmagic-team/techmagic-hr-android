package co.techmagic.hr.data.store;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface IEmployeeApi {

    @GET("v1/users")
    Observable<Employee> getEmployees(@Query("q") String searchQuery, @Query("_department") String departmentId, @Query("lastWorkingDay") boolean lastWorkingDay, @Query("lead") String leadId, @Query("offset") int offset, @Query("limit") int limit);

    @GET("v1/departments")
    Observable<List<Filter>> getFilterDepartments();

    @GET("v1/endpoints/leads-with-employees")
    Observable<List<FilterLead>> getFilterLeads();

    @GET("v1/user-group/project")
    Observable<List<Filter>> getFilterProjects();

    /* Used for employee details */

    @GET("/v1/time-off/vacation/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserVacations(@Path("userId") String userId, @Query("isPaid") boolean isPaid, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("/v1/time-off/vacation/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserDayOffs(@Path("userId") String userId, @Query("isPaid") boolean isPaid, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("/v1/time-off/illness/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserIllnesses(@Path("userId") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    /* Used for calendar */

    @GET("/v1/time-off/vacation?isPaid=true")
    Observable<List<RequestedTimeOff>> getAllVacations(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("/v1/time-off/vacation?isPaid=false")
    Observable<List<RequestedTimeOff>> getAllDayOffs(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("/v1/time-off/illness")
    Observable<List<RequestedTimeOff>> getAllIllnesses(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("/v1/users")
    Observable<Employee> getAllEmployeesByDepartment(@Query("project") String projectId, @Query("_department") String departmentId, @Query("my-team") boolean isMyTeam);

    @GET("/v1/endpoints/calendar")
    Observable<List<CalendarInfo>> getCalendar(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);
}