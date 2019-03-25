package co.techmagic.hr.presentation.login

import co.techmagic.hr.domain.repository.IUserRepository
import co.techmagic.hr.presentation.ui.manager.AccountManager
import com.techmagic.viper.base.BasePresenter


class HrAppLoginPresenter(private val userRepository: IUserRepository,
                          private val accountManager: AccountManager) : BasePresenter<LoginView, LoginRouter>(), LoginPresenter {

    override fun handleLoginClick(googleAuthToken: String) {
        userRepository.googleLogin(googleAuthToken)
                .subscribe({
                    accountManager.saveUser(it)
                    router?.onSuccessfulLogin()
                }, {
                    // TODO add error handling
                    view?.showMessage("" + it.message)
                    it.printStackTrace()
                })
    }
}