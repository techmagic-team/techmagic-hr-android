package co.techmagic.hr.data.repository;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private ApiClient client;


    public EmployeeRepositoryImpl() {
        client = ApiClient.getApiClient();
    }


    @Override
    public Observable<Employee> getEmployees() {
        return client.getEmployeeClient().getEmployees();
    }
}
