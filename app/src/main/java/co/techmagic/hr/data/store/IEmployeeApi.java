package co.techmagic.hr.data.store;

import java.util.List;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.data.request.TimeOffRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface IEmployeeApi {

    @GET("v1/users")
    Observable<Employee> getEmployees(@Query("q") String searchQuery, @Query("_department") String departmentId, @Query("lastWorkingDay") boolean lastWorkingDay, @Query("lead") String leadId, @Query("offset") int offset, @Query("limit") int limit);

    @GET("v1/departments")
    Observable<List<FilterDepartment>> getFilterDepartments();

    @GET("v1/endpoints/leads-with-employees")
    Observable<List<FilterLead>> getFilterLeads();

    @POST("/v1/time-off/vacation/findAll/")
    Observable<List<RequestedTimeOff>> getAllVacations(@Body TimeOffRequest request);

    @POST("/v1/time-off/illness/findAll/")
    Observable<List<RequestedTimeOff>> getAllIllnesses(@Body GetIllnessRequest request);
}