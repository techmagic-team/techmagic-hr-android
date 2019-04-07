package co.techmagic.hr.presentation.login

import com.techmagic.viper.Router

interface LoginRouter : Router {

    fun onSuccessfulLogin()
}