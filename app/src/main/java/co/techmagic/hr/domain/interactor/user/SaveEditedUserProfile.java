package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;


public class SaveEditedUserProfile extends DataUseCase<EditProfileRequest, UserProfile, IUserRepository> {


    public SaveEditedUserProfile(IUserRepository iUserRepository) {
        super(iUserRepository);
    }


    @Override
    protected Observable<UserProfile> buildObservable(EditProfileRequest editProfileRequest) {
        return repository.saveEditedProfile(editProfileRequest);
    }
}