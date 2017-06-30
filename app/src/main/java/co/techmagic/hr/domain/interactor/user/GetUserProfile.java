package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

public class GetUserProfile extends DataUseCase<GetMyProfileRequest, UserProfile, IUserRepository> {

    public GetUserProfile(IUserRepository iUserRepository) {
        super(iUserRepository);
    }

    @Override
    protected Observable<UserProfile> buildObservable(GetMyProfileRequest getMyProfileRequest) {
        return repository.getMyProfile(getMyProfileRequest);
    }
}