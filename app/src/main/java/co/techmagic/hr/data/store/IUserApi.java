package co.techmagic.hr.data.store;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.LoginRequest;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by techmagic on 8/30/16.
 */
public interface IUserApi {

    @POST("v1/auth")
    Observable<User> login(@Body LoginRequest loginRequest);

    @POST("v1/auth/forgot-password")
    Observable<Void> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);
}
