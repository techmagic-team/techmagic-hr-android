package co.techmagic.hr.presentation.login

import com.techmagic.viper.View

interface LoginView : View {

    fun showProgress(show: Boolean)
}