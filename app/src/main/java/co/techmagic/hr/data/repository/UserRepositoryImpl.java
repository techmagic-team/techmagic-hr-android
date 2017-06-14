package co.techmagic.hr.data.repository;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.UserProfile;
import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.exception.NetworkConnectionException;
import co.techmagic.hr.data.manager.NetworkManager;
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl;
import co.techmagic.hr.data.request.EditProfileRequest;
import co.techmagic.hr.data.request.ForgotPasswordRequest;
import co.techmagic.hr.data.request.GetMyProfileRequest;
import co.techmagic.hr.data.request.LoginRequest;
import co.techmagic.hr.data.request.UploadPhotoRequest;
import co.techmagic.hr.data.store.client.ApiClient;
import co.techmagic.hr.domain.repository.IUserRepository;
import rx.Observable;

/**
 * Created by techmagic on 8/31/16.
 */
public class UserRepositoryImpl implements IUserRepository {

    private ApiClient client;
    private NetworkManager networkManager;


    public UserRepositoryImpl() {
        client = ApiClient.getApiClient();
        networkManager = NetworkManagerImpl.getNetworkManager();
    }


    @Override
    public Observable<User> login(LoginRequest loginRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().login(loginRequest);
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<Void> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().forgotPassword(forgotPasswordRequest);
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<UserProfile> getMyProfile(GetMyProfileRequest getMyProfileRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().getMyProfile(getMyProfileRequest.getUserId());
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<List<Company>> getCompanies() {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().getCompanies();
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<UserProfile> saveEditedProfile(EditProfileRequest editProfileRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().saveEditedProfile(editProfileRequest.getId(), editProfileRequest);
        }

        return Observable.error(new NetworkConnectionException());
    }


    @Override
    public Observable<Void> uploadPhoto(UploadPhotoRequest uploadPhotoRequest) {
        if (networkManager.isNetworkAvailable()) {
            return client.getUserApiClient().uploadPhoto(uploadPhotoRequest.getUserId(), uploadPhotoRequest.getMultipartBody());
        }

        return Observable.error(new NetworkConnectionException());
    }
}
