package co.techmagic.hr.domain.interactor.employee;

import java.util.List;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetUserIllness extends DataUseCase <GetIllnessRequest, List<RequestedTimeOff>, IEmployeeRepository>{


    public GetUserIllness(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<List<RequestedTimeOff>> buildObservable(GetIllnessRequest request) {
        return repository.getUserIllnesses(request);
    }
}
