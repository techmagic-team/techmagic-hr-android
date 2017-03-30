package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.Employee;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetEmployee extends EmptyUseCase<Employee, IEmployeeRepository> {

    private int offset = 0;
    private int limit = 0;

    public GetEmployee(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    public void setOffset(int offset) {
        this.offset = offset;
    }


    public void setLimit(int limit) {
        this.limit = limit;
    }


    @Override
    protected Observable<Employee> buildObservable() {
        return repository.getEmployees(offset, limit);
    }
}
