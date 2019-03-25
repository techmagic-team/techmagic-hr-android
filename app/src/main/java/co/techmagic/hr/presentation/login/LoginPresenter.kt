package co.techmagic.hr.presentation.login

import com.techmagic.viper.Presenter

interface LoginPresenter : Presenter {

    fun handleLoginClick(googleAuthToken: String)
}