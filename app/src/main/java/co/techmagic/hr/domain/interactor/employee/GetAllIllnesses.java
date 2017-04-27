package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.CalendarInfo;
import co.techmagic.hr.data.request.TimeOffAllRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

/**
 * Created by techmagic on 4/27/17.
 */

public class GetAllIllnesses extends DataUseCase<TimeOffAllRequest, List<CalendarInfo>, IEmployeeRepository> {

    public GetAllIllnesses(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<List<CalendarInfo>> buildObservable(TimeOffAllRequest timeOffAllRequest) {
        return repository.getAllIllnesses(timeOffAllRequest);
    }
}
