package co.techmagic.hr.data.store;

import co.techmagic.hr.data.entity.Employee;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface IEmployeeApi {

    @GET("v1/users?q=&_department=&lastWorkingDay=false&lead")
    Observable<Employee> getEmployees(@Query("offset") int offset, @Query("limit") int limit);
}
