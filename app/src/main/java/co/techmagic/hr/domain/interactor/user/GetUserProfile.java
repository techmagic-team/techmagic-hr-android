package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

public class GetUserProfile extends DataUseCase<GetMyProfileRequest, Docs, IUserRepository> {

    public GetUserProfile(IUserRepository iUserRepository) {
        super(iUserRepository);
    }

    @Override
    protected Observable<Docs> buildObservable(GetMyProfileRequest getMyProfileRequest) {
        return repository.getMyProfile(getMyProfileRequest);
    }
}