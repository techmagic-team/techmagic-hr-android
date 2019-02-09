package com.techmagic.viper.base

import android.support.annotation.CallSuper

import com.techmagic.viper.Presentable
import com.techmagic.viper.Presenter
import com.techmagic.viper.Router
import com.techmagic.viper.View
import com.techmagic.viper.ViewState

import rx.subscriptions.CompositeSubscription

abstract class BasePresenter<VIEW : View, ROUTER : Router> : Presenter {

    var view: VIEW? = null
        set(view) {
            val previousView = field
            field = view
            if (previousView != null && previousView !== view) {
                previousView.destroyView()
                previousView.detachViewOutput()
            }
        }

    private var router: ROUTER? = null

    protected var initial: Boolean = false

    private val compositeSubscription = CompositeSubscription()

    @CallSuper
    override fun onViewCreated(isInitial: Boolean) {
        validate()
    }

    @CallSuper
    override fun onViewStarted() {
    }

    @CallSuper
    override fun onViewResumed() {
    }

    @CallSuper
    override fun onViewPaused() {
    }

    @CallSuper
    override fun onViewStopped() {
    }

    @CallSuper
    override fun onViewDestroyed() {
        compositeSubscription.clear()
    }

    @CallSuper
    override fun onViewDetached() {
        compositeSubscription.clear()
        view = null
        //router = null;
    }

    @CallSuper
    override fun onStateSaved(): ViewState? = null

    @CallSuper
    override fun onStateRestored(state: ViewState) {
    }

    @CallSuper
    protected fun validate() {
        checkField("view", view)
    }

    protected fun checkField(fieldName: String, field: Any?) {
        if (field == null) {
            throw IllegalStateException(String.format("%s should not be null!", fieldName))
        }
    }

    companion object {
        fun <R : Router, P : BasePresenter<V, R>, V : View> bind(view: V, presenter: P, router: R) {
            presenter.view = view
            presenter.router = router
            if (view is Presentable<*>) {
                (view as Presentable<P>).setPresenter(presenter)
            }
        }
    }
}
