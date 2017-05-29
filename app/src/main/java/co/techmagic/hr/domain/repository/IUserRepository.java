package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.LoginRequest;
import rx.Observable;

public interface IUserRepository {

    // String STUB_COMPANY_ID = "58593c3398c899445ecb224a"; // Testing Company (production)
    String STUB_COMPANY_ID = "585019ffff9c2c5a2b98a7ce";
   // String STUB_COMPANY_ID = "583457c28725f715d8f010fd"; // Production Company

    Observable<User> login(LoginRequest loginRequest);

    Observable<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Observable<Docs> getMyProfile(GetMyProfileRequest getMyProfileRequest);

    Observable<List<Company>> getCompanies();
}
