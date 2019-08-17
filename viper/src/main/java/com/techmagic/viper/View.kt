package com.techmagic.viper

import android.support.annotation.StringRes

interface View {
    fun initView()
    fun destroyView()
    fun detachViewOutput()
    fun showErrorMessage(@StringRes messageRes: Int)
    fun showErrorMessage(message: String)
}
