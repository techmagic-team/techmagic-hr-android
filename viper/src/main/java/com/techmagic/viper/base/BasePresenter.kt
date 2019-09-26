package com.techmagic.viper.base

import android.support.annotation.CallSuper
import com.techmagic.viper.*
import rx.Observable
import rx.Single
import rx.Subscription
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

    protected var router: ROUTER? = null

    protected var initial: Boolean = false

    @Deprecated("Move logic with composite subscriptions to app specific BasePresenter")
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

    @Deprecated("Move logic with composite subscriptions to app specific BasePresenter")
    protected fun <T> call(
            observable: Observable<T>,
            onSuccess: (value: T) -> Unit,
            onError: (throwable: Throwable) -> Unit = {
                it.message?.let {
                    view?.showErrorMessage(it)
                }
            }
    ): Subscription {
        val subscription = observable.subscribe({ onSuccess(it) }, { onError(it) })
        compositeSubscription.add(subscription)
        return subscription
    }

    @Deprecated("Move logic with composite subscriptions to app specific BasePresenter")
    protected fun <T> call(
            single: Single<T>,
            onSuccess: (value: T) -> Unit,
            onError: (throwable: Throwable) -> Unit = {
                it.message?.let {
                    view?.showErrorMessage(it)
                }
            }
    ): Subscription {
        val subscription = single.subscribe({ onSuccess(it) }, { onError(it) })
        compositeSubscription.add(subscription)
        return subscription
    }

    protected fun checkField(fieldName: String, field: Any?) {
        if (field == null) {
            throw IllegalStateException(String.format("%s should not be null!", fieldName))
        }
    }

    protected fun showError(t: Throwable) {
        view?.showErrorMessage(t.localizedMessage ?: "Error occurred!")
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
