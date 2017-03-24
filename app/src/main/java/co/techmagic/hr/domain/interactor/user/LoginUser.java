package co.techmagic.hr.domain.interactor.user;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.domain.interactor.DataUseCase;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

/**
 * Created by techmagic on 8/31/16.
 */
public class LoginUser extends DataUseCase<LoginRequest, User, IUserRepository> {

    public LoginUser(IUserRepository repository) {
        super(repository);
    }

    @Override
    protected Observable<User> buildObservable(LoginRequest loginRequest) {
        return repository.login(loginRequest);
    }
}
