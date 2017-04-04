package co.techmagic.hr.data.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.entity.FilterDepartment;
import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

/**
 * Created by techmagic on 4/3/17.
 */

public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private ApiClient client;

    public EmployeeRepositoryImpl() {
        client = ApiClient.getApiClient();
    }


    @Override
    public Observable<Employee> getEmployees(EmployeeFiltersRequest request) {
        return client.getEmployeeClient().getEmployees(request.getQuery(), request.getDepartmentId(), request.isLastWorkingDay(), request.getLeadId(), request.getOffset(), request.getLimit());
    }


    @Override
    public Observable<List<FilterDepartment>> getFilterDepartments() {
        return client.getEmployeeClient().getFilterDepartments();
    }


    @Override
    public Observable<List<FilterLead>> getFilterLeads() {
        return client.getEmployeeClient().getFilterLeads();
    }
}
