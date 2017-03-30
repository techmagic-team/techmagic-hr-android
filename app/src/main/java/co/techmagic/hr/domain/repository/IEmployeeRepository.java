package co.techmagic.hr.domain.repository;

import co.techmagic.hr.data.entity.Employee;
import rx.Observable;

public interface IEmployeeRepository {

    String STUB_COMPANY_ID = "585019ffff9c2c5a2b98a7ce";

    Observable<Employee> getEmployees(int offset, int limit);
}
