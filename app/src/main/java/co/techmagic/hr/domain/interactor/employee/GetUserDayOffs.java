package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.TimeOffRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetUserDayOffs extends DataUseCase<TimeOffRequest, List<RequestedTimeOff>, IEmployeeRepository> {


    public GetUserDayOffs(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }

    @Override
    protected Observable<List<RequestedTimeOff>> buildObservable(TimeOffRequest request) {
        return repository.getUserDayOffs(request);
    }
}