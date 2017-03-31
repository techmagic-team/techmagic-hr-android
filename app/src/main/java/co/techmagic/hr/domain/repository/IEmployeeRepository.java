package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import rx.Observable;

public interface IEmployeeRepository {

    String STUB_COMPANY_ID = "585019ffff9c2c5a2b98a7ce";

    Observable<Employee> getEmployees(int offset, int limit);

    Observable<List<FilterDepartment>> getFilterDepartments();

    Observable<List<FilterLead>> getFilterLeads();
}
