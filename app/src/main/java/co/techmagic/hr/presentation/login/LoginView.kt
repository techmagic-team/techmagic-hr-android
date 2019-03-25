package co.techmagic.hr.presentation.login

import com.techmagic.viper.View

interface LoginView : View {

    fun showMessage(msg: String)
}