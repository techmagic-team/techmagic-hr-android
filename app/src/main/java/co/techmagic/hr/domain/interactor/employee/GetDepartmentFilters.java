package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.Filter;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetDepartmentFilters extends EmptyUseCase<List<Filter>, IEmployeeRepository> {


    public GetDepartmentFilters(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }

    @Override
    protected Observable<List<Filter>> buildObservable() {
        return repository.getFilterDepartments();
    }
}
