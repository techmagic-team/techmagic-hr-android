package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetTimeOff extends DataUseCase<TimeOffRequest, RequestedTimeOff[], IEmployeeRepository> {


    public GetTimeOff(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }

    @Override
    protected Observable<RequestedTimeOff[]> buildObservable(TimeOffRequest request) {
        return repository.getAllVacations(request);
    }
}