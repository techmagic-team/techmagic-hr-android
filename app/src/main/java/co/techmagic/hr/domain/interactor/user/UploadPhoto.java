package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.request.UploadPhotoRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;


public class UploadPhoto extends DataUseCase<UploadPhotoRequest, Void, IUserRepository> {


    public UploadPhoto(IUserRepository iUserRepository) {
        super(iUserRepository);
    }


    @Override
    protected Observable<Void> buildObservable(UploadPhotoRequest uploadPhotoRequest) {
        return repository.uploadPhoto(uploadPhotoRequest);
    }
}