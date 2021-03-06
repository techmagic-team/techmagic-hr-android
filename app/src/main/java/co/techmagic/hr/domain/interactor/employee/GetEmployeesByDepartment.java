package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.data.request.EmployeesByDepartmentRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

/**
 * Created by techmagic on 4/27/17.
 */

public class GetEmployeesByDepartment extends DataUseCase<EmployeesByDepartmentRequest, Employee, IEmployeeRepository>{

    public GetEmployeesByDepartment(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<Employee> buildObservable(EmployeesByDepartmentRequest employeesByDepartmentRequest) {
        return repository.getAllEmployeesByDepartment(employeesByDepartmentRequest);
    }
}
