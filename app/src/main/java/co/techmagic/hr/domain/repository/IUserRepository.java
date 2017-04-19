package co.techmagic.hr.domain.repository;

import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.LoginRequest;
import rx.Observable;

public interface IUserRepository {

    String STUB_COMPANY_ID = "58593c3398c899445ecb224a"; // Testing Company

    Observable<User> login(LoginRequest loginRequest);

    Observable<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Observable<Docs> getMyProfile(GetMyProfileRequest getMyProfileRequest);
}
