package co.techmagic.hr.domain.repository;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.LoginRequest;
import rx.Observable;

public interface IUserRepository {

    String STUB_COMPANY_ID = "585019ffff9c2c5a2b98a7ce";

    Observable<User> login(LoginRequest loginRequest);
}
