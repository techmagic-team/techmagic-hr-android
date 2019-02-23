package co.techmagic.hr.presentation.login

import co.techmagic.hr.domain.repository.IUserRepository
import com.techmagic.viper.base.BasePresenter


class HrAppLoginPresenter(val userRepository: IUserRepository) : BasePresenter<LoginView, LoginRouter>(), LoginPresenter {

    override fun handleLoginClick(email: String) {
        // todo refactor
//        SharedPreferencesUtil.saveAccessToken(user.getAccessToken())
//        SharedPreferencesUtil.saveUser(user)
    }
}