package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.FilterLead;
import co.techmagic.hr.domain.interactor.EmptyUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetLeadFilters extends EmptyUseCase<List<FilterLead>, IEmployeeRepository> {


    public GetLeadFilters(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<List<FilterLead>> buildObservable() {
        return repository.getFilterLeads();
    }
}
