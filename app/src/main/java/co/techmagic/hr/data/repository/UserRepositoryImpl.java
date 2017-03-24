package co.techmagic.hr.data.repository;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

/**
 * Created by techmagic on 8/31/16.
 */
public class UserRepositoryImpl implements IUserRepository {

    private ApiClient client;


    public UserRepositoryImpl() {
        client = ApiClient.getApiClient();
    }


    @Override
    public Observable<User> login(LoginRequest loginRequest) {
        return client.getUserApiClient().login(loginRequest);
    }
}
