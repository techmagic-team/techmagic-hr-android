package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.entity.EditProfile;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;


public class SaveEditedUserProfile extends DataUseCase<EditProfileRequest, EditProfile, IUserRepository> {


    public SaveEditedUserProfile(IUserRepository iUserRepository) {
        super(iUserRepository);
    }


    @Override
    protected Observable<EditProfile> buildObservable(EditProfileRequest editProfileRequest) {
        return repository.saveEditedProfile(editProfileRequest);
    }
}