package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import rx.Observable;

public interface IEmployeeRepository {

    String STUB_COMPANY_ID = "585019ffff9c2c5a2b98a7ce";

    Observable<Employee> getEmployees(EmployeeFiltersRequest request);

    Observable<List<FilterDepartment>> getFilterDepartments();

    Observable<List<FilterLead>> getFilterLeads();
}
