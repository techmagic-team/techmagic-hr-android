package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

/**
 * Created by techmagic on 4/27/17.
 */

public class GetAllDayOffs extends DataUseCase<TimeOffAllRequest, List<RequestedTimeOff>, IEmployeeRepository> {

    public GetAllDayOffs(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<List<RequestedTimeOff>> buildObservable(TimeOffAllRequest timeOffAllRequest) {
        return repository.getAllDayOffs(timeOffAllRequest);
    }
}
