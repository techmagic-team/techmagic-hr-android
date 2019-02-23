package com.techmagic.viper

interface Presentable<PRESENTER : Presenter> {
    fun providePresenter(presenter: PRESENTER)
}
