package co.techmagic.hr.domain.interactor.employee;

import co.techmagic.hr.data.entity.RequestedTimeOff;
import co.techmagic.hr.data.request.GetIllnessRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IEmployeeRepository;
import rx.Observable;

public class GetIllness extends DataUseCase <GetIllnessRequest, RequestedTimeOff[], IEmployeeRepository>{


    public GetIllness(IEmployeeRepository iEmployeeRepository) {
        super(iEmployeeRepository);
    }


    @Override
    protected Observable<RequestedTimeOff[]> buildObservable(GetIllnessRequest request) {
        return repository.getAllIllnesses(request);
    }
}
