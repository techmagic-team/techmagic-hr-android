package co.techmagic.hr.data.store;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.entity.DatePeriod;
import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.HolidayDate;
import co.techmagic.hr.data.entity.RequestTimeOff;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.entity.TimeOffAmount;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface IEmployeeApi {

    @GET("users")
    Observable<Employee> getEmployees(@Query("q") String searchQuery, @Query("project") String projectId, @Query("_department") String departmentId,
                                      @Query("lastWorkingDay") boolean lastWorkingDay, @Query("lead") String leadId, @Query("offset") int offset, @Query("limit") int limit);

    @Deprecated
    @GET("departments")
    Observable<List<Filter>> getFilterDepartments();

    @GET("endpoints/leads-with-employees")
    Observable<List<FilterLead>> getFilterLeadsWithEmployees();

    @GET("user-group/project")
    Observable<List<Filter>> getFilterProjects();

    /* Used for employee details */

    @GET("time-off/vacation/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserVacations(@Path("userId") String userId, @Query("isPaid") boolean isPaid, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/vacation/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserDayOffs(@Path("userId") String userId, @Query("isPaid") boolean isPaid, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/illness/user/{userId}")
    Observable<List<RequestedTimeOff>> getUserIllnesses(@Path("userId") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("rooms")
    Observable<List<Filter>> getFilterRooms();

    @GET("leads")
    Observable<List<FilterLead>> getLeads();

    @GET("reasons")
    Observable<List<Filter>> getFilterReasons();

    /* Used for calendar */

    @GET("time-off/vacation?isPaid=true")
    Observable<List<RequestedTimeOff>> getAllVacations(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/vacation?isPaid=false")
    Observable<List<RequestedTimeOff>> getAllDayOffs(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/illness")
    Observable<List<RequestedTimeOff>> getAllIllnesses(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("users")
    Observable<Employee> getAllEmployeesByDepartment(@Query("project") String projectId, @Query("_department") String departmentId, @Query("my-team") boolean isMyTeam);

    @GET("endpoints/calendar")
    Observable<List<CalendarInfo>> getCalendar(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/vacation/user/{user_id}?isPaid=true")
    Observable<List<RequestedTimeOff>> getUsedVacationsByUser(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/vacation/user/{user_id}?isPaid=false")
    Observable<List<RequestedTimeOff>> getUsedDayOffsByUser(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/illness/user/{user_id}")
    Observable<List<RequestedTimeOff>> getUsedIllnessesByUser(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    /* Used for request time off */

    @GET("time-off/vacation/user/{user_id}/totaldays?isPaid=true")
    Observable<TimeOffAmount> getTotalVacation(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/vacation/user/{user_id}/totaldays?isPaid=false")
    Observable<TimeOffAmount> getTotalDayOff(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/illness/user/{user_id}/totaldays?isPaid=true")
    Observable<TimeOffAmount> getTotalIllness(@Path("user_id") String userId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("holidays")
    Observable<List<HolidayDate>> getHolidays(@Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo);

    @GET("time-off/dates/{user_id}")
    Observable<List<DatePeriod>> getUserPeriod(@Path("user_id") String userId);

    @POST("time-off/vacation")
    Observable<RequestedTimeOff> requestVacation(@Body RequestTimeOff requestTimeOff);

    @POST("time-off/illness")
    Observable<RequestedTimeOff> requestIllness(@Body RequestTimeOff requestTimeOff);

    @DELETE("time-off/vacation/{timeOff_id}")
    Observable<Void> deleteTimeOff(@Path("timeOff_id") String timeOffId);

    @DELETE("time-off/illness/{timeOff_id}")
    Observable<Void> deleteIllness(@Path("timeOff_id") String timeOffId);
}