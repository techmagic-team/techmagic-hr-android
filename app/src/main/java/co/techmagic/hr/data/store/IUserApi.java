package co.techmagic.hr.data.store;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.entity.UserProfile;
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
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by techmagic on 8/30/16.
 */
public interface IUserApi {

    @GET("google-auth")
    Observable<User> googleLogin(@Query("code") String googleAuthToken);

    @POST("auth")
    Observable<User> login(@Body LoginRequest loginRequest);

    @POST("auth/forgot-password")
    Observable<Void> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @GET("users/{id}")
    Observable<UserProfile> getMyProfile(@Path("id") String userId);

    @GET("companies")
    Observable<List<Company>> getCompanies();

    @PATCH("users/{id}")
    Observable<UserProfile> saveEditedProfile(@Path("id") String userId, @Body EditProfileRequest editProfileRequest);

    @Multipart
    @POST("users/{id}/photo")
    Observable<Void> uploadPhoto(@Path("id") String userId, @Part MultipartBody.Part image);
}
