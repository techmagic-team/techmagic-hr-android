package co.techmagic.hr.data.store;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.LoginRequest;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by techmagic on 8/30/16.
 */
public interface IUserApi {

    @POST("v1/auth")
    Observable<User> login(@Body LoginRequest loginRequest);

    @POST("v1/auth/forgot-password")
    Observable<Void> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @GET("/v1/users/{id}")
    Observable<UserProfile> getMyProfile(@Path("id") String userId);

    @GET("/v1/companies")
    Observable<List<Company>> getCompanies();

    @PATCH("/v1/users/{id}")
    Observable<UserProfile> saveEditedProfile(@Path("id") String userId, @Body EditProfileRequest editProfileRequest);

    @Multipart
    @POST("/v1/users/{id}/photo")
    Observable<Void> uploadPhoto(@Path("id") String userId, @Part MultipartBody.Part image);
}
