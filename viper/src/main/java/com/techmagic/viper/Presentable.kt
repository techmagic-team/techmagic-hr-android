package com.techmagic.viper

interface Presentable<PRESENTER : Presenter> {
    fun setPresenter(presenter: PRESENTER)
}
