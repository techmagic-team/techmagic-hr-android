package co.techmagic.hr.presentation.mvp.base

import android.os.Bundle
import android.view.View
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.AnimatedProgressDialog
import co.techmagic.hr.presentation.util.UiUtil
import com.techmagic.viper.Presenter
import com.techmagic.viper.base.BaseViewFragment

open class HrAppBaseViewFragment<PRESENTER : Presenter> : BaseViewFragment<PRESENTER>(), ProgressableView {
    private lateinit var animatedProgressDialog: AnimatedProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animatedProgressDialog = AnimatedProgressDialog(context, R.style.DialogThemeDimmed)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnKeyboardVisibilityChanges()
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            animatedProgressDialog.show()
        } else {
            animatedProgressDialog.hide()
        }
    }

    protected open fun hideKeyboardOnTouchOutside() {
        UiUtil.setupHideKeyboardOnTouchRecursively(activity?.window?.decorView)
    }

    protected open fun onKeyboardOpened() {

    }

    protected open fun onKeyboardClosed() {

    }

    private fun subscribeOnKeyboardVisibilityChanges() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            view ?: return@addOnGlobalLayoutListener
            val heightDiff = view!!.rootView.height - view!!.height
            if (heightDiff > UiUtil.dp2Px(200F)) {
                onKeyboardOpened()
            } else {
                onKeyboardClosed()
            }
        }
    }
}