package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.data.request.EmployeeFiltersRequest;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetEmployee extends DataUseCase<EmployeeFiltersRequest, Employee, IEmployeeRepository> {


    public GetEmployee(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<Employee> buildObservable(EmployeeFiltersRequest request) {
        return repository.getEmployees(request);
    }
}
