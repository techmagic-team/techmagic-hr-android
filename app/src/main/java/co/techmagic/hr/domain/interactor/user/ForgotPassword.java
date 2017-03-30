package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;


public class ForgotPassword extends DataUseCase<ForgotPasswordRequest, Void, IUserRepository> {

    public ForgotPassword(IUserRepository repository) {
        super(repository);
    }


    @Override
    protected Observable<Void> buildObservable(ForgotPasswordRequest forgotPasswordRequest) {
        return repository.forgotPassword(forgotPasswordRequest);
    }
}