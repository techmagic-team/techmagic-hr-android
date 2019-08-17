package co.techmagic.hr.presentation.base

import android.support.annotation.StringRes
import com.techmagic.viper.Router

interface HrTmRouter : Router {
    fun showInfoDialog(@StringRes title: Int, @StringRes description: Int)
}