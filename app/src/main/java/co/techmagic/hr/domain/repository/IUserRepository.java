package co.techmagic.hr.domain.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.Docs;
import co.techmagic.hr.data.entity.EditProfile;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.data.request.UploadPhotoRequest;
import rx.Observable;

public interface IUserRepository {

    Observable<User> login(LoginRequest loginRequest);

    Observable<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    Observable<Docs> getMyProfile(GetMyProfileRequest getMyProfileRequest);

    Observable<List<Company>> getCompanies();

    Observable<EditProfile> saveEditedProfile(EditProfileRequest editProfileRequest);

    Observable<Void> uploadPhoto(UploadPhotoRequest uploadPhotoRequest);
}
