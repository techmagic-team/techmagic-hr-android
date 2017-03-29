package co.techmagic.hr.data.store;

import co.techmagic.hr.data.entity.Employee;
import retrofit2.http.GET;
import rx.Observable;

public interface IEmployeeApi {

    @GET("v1/users")
    Observable<Employee> getEmployees();
}
