package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetUserVacations extends DataUseCase<TimeOffRequest, List<RequestedTimeOff>, IEmployeeRepository> {


    public GetUserVacations(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }

    @Override
    protected Observable<List<RequestedTimeOff>> buildObservable(TimeOffRequest request) {
        return repository.getUserVacations(request);
    }
}