package com.techmagic.viper.base

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.techmagic.viper.Presentable
import com.techmagic.viper.Presenter
import com.techmagic.viper.ViewState


abstract class BaseViewFragment<PRESENTER : Presenter> : Fragment(), com.techmagic.viper.View, Presentable<PRESENTER> {

    private val TAG = javaClass.name

    private var _presenter: PRESENTER? = null

    protected val presenter: PRESENTER?
        get() = _presenter

    var isRestored: Boolean = false
        private set

    override fun setPresenter(presenter: PRESENTER) {
        this._presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isRestored = savedInstanceState != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restoreState(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter?.onViewCreated(savedInstanceState == null)
    }

    override fun initView() {}

    override fun onSaveInstanceState(outState: Bundle) {
        saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewStarted()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onViewResumed()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onViewPaused()
    }

    override fun onStop() {
        super.onStop()
        this.isRestored = false
        presenter?.onViewStopped()
    }

    override fun destroyView() {
        presenter?.onViewDestroyed()
        if (view != null) {
            unbindClickListeners(view)
        }
    }

    override fun detachViewOutput() {
        presenter?.onViewDetached()
        _presenter = null
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        detachViewOutput()
    }

    protected fun restoreState(savedInstanceState: Bundle?) {
        val stateParcelable = savedInstanceState?.getParcelable<Parcelable>(KEY_SAVED_STATE)
        if (stateParcelable != null) {
            val state = convert(stateParcelable)
            if (state != null) {
                presenter?.onStateRestored(state)
            }
        }
    }

    protected fun saveState(outState: Bundle) {
        val state = presenter?.onStateSaved()
        if (state != null) {
            outState.putParcelable(KEY_SAVED_STATE, convert(state))
        }
    }

    protected fun unbindClickListeners(view: View?) {
        view?.setOnClickListener(null)

        if (view is ViewGroup) {
            val childCount = view.childCount
            for (i in 0 until childCount) {
                val child = view.getChildAt(i)
                unbindClickListeners(child)
            }
        }
    }

    protected inline fun <reified T : Fragment> getFragment(@IdRes id: Int): T? {
        return childFragmentManager.findFragmentById(id) as? T
    }

    protected fun replaceFragment(@IdRes id: Int, fragment: Fragment, shouldAddToBackStack: Boolean) {
        replaceFragment(id, fragment, childFragmentManager, shouldAddToBackStack)
    }

    protected fun replaceFragment(@IdRes id: Int, fragment: Fragment, fragmentManager: FragmentManager, shouldAddToBackStack: Boolean) {
        val tag = fragment.javaClass.canonicalName
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment, tag)
        if (shouldAddToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    protected fun convert(state: ViewState?): Parcelable? {
        return null
    }

    protected fun convert(state: Parcelable?): ViewState? {
        return null
    }

    companion object {
        protected const val KEY_SAVED_STATE = "key_saved_state"
    }
}
