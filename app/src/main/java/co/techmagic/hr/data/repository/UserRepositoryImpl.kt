package co.techmagic.hr.data.repository

import co.techmagic.hr.data.entity.Company
import co.techmagic.hr.data.entity.User
import co.techmagic.hr.data.entity.UserProfile
import co.techmagic.hr.data.manager.NetworkManager
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.request.EditProfileRequest
import co.techmagic.hr.data.request.ForgotPasswordRequest
import co.techmagic.hr.data.request.GetMyProfileRequest
import co.techmagic.hr.data.request.LoginRequest
import co.techmagic.hr.data.request.UploadPhotoRequest
import co.techmagic.hr.data.store.IUserApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.domain.repository.IUserRepository
import rx.Observable


class UserRepositoryImpl(
        networkManager: NetworkManager,
        private val userApiClient: IUserApi,
        @Deprecated("Use only one API client")
        private val serializableUserApiClient: IUserApi) :
        BaseNetworkRepository(networkManager), IUserRepository {

    @Deprecated("Remove when DI is set up")
    constructor() : this(NetworkManagerImpl.getNetworkManager(),
            ApiClient.getApiClient().userApiClient,
            ApiClient.getApiClient().serializableNullsUserClient)

    override fun login(loginRequest: LoginRequest): Observable<User> {
        return setup(userApiClient.login(loginRequest))
    }

    override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Observable<Void> {
        return setup(userApiClient.forgotPassword(forgotPasswordRequest))
    }

    override fun getMyProfile(getMyProfileRequest: GetMyProfileRequest): Observable<UserProfile> {
        return setup(userApiClient.getMyProfile(getMyProfileRequest.userId))
    }

    override fun getCompanies(): Observable<List<Company>> {
        return setup(userApiClient.companies)
    }

    /**
     * [.serializableUserApiClient] should be used here for serialization
     */
    override fun saveEditedProfile(editProfileRequest: EditProfileRequest): Observable<UserProfile> {
        return setup(serializableUserApiClient.saveEditedProfile(editProfileRequest.id, editProfileRequest))
    }

    override fun uploadPhoto(uploadPhotoRequest: UploadPhotoRequest): Observable<Void> {
        return setup(userApiClient.uploadPhoto(uploadPhotoRequest.userId, uploadPhotoRequest.multipartBody))
    }
}
