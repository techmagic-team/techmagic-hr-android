package co.techmagic.hr.presentation.login

import co.techmagic.hr.domain.repository.IUserRepository
import co.techmagic.hr.presentation.ui.manager.AccountManager
import com.techmagic.viper.base.BasePresenter


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
                    view?.handleError(it)
                    view?.showProgress(false)
                })
    }
}