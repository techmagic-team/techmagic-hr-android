package co.techmagic.hr.presentation.login

import co.techmagic.hr.R
import co.techmagic.hr.data.exception.NetworkConnectionException
import co.techmagic.hr.domain.repository.IUserRepository
import co.techmagic.hr.presentation.ui.manager.AccountManager
import com.techmagic.viper.base.BasePresenter
import retrofit2.HttpException
import java.net.SocketTimeoutException


class HrAppLoginPresenter(private val userRepository: IUserRepository,
                          private val accountManager: AccountManager) : BasePresenter<LoginView, LoginRouter>(), LoginPresenter {

    override fun handleLoginClick(googleAuthToken: String) {
        view?.showProgress(true)
        userRepository.googleLogin(googleAuthToken)
                .subscribe({
                    accountManager.saveUser(it)
                    // view?.showProgress(false) // will be hidden during redirect
                    router?.onSuccessfulLogin()
                }, {
                    handleLoginError(it)
                    view?.showProgress(false)
                })
    }

    private fun handleLoginError(throwable: Throwable) {
        when (throwable) {
            is NetworkConnectionException, is SocketTimeoutException -> view?.showErrorMessage("Error " + throwable.message)
            is HttpException -> view?.showErrorMessage(R.string.message_error_user_does_not_exist)
            else -> view?.showErrorMessage(R.string.message_error_unknown)
        }
    }
}