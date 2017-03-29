package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetEmployee extends EmptyUseCase<Employee, IEmployeeRepository> {


    public GetEmployee(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<Employee> buildObservable() {
        return repository.getEmployees();
    }
}
