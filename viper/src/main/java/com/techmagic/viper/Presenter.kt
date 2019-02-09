package com.techmagic.viper

interface Presenter {
    fun onViewCreated(isInitial: Boolean)

    fun onViewStarted()

    fun onViewResumed()

    fun onViewPaused()

    fun onViewStopped()

    fun onViewDestroyed()

    fun onViewDetached()

    fun onStateSaved(): ViewState?

    fun onStateRestored(state: ViewState)
}
