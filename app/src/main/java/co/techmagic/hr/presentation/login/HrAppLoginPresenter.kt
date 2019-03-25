package co.techmagic.hr.presentation.login

import co.techmagic.hr.domain.repository.IUserRepository
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import com.techmagic.viper.base.BasePresenter


class HrAppLoginPresenter(private val userRepository: IUserRepository) : BasePresenter<LoginView, LoginRouter>(), LoginPresenter {

    override fun handleLoginClick(googleAuthToken: String) {
        // todo implement correct logic handling
        userRepository.googleLogin(googleAuthToken)
                .subscribe( {
//                    SharedPreferencesUtil.saveAccessToken(it.accessToken)
//                    SharedPreferencesUtil.saveUser(it)
                    view?.showMessage("Logged in")
                }, {
                    view?.showMessage(""+it.message)
                    it.printStackTrace()
                })
    }
}