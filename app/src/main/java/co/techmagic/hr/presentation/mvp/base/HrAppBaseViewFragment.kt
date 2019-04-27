package co.techmagic.hr.presentation.mvp.base

import android.os.Bundle
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.AnimatedProgressDialog
import com.techmagic.viper.Presenter
import com.techmagic.viper.base.BaseViewFragment

open class HrAppBaseViewFragment<PRESENTER : Presenter> : BaseViewFragment<PRESENTER>(), ProgressableView {
    private lateinit var animatedProgressDialog: AnimatedProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animatedProgressDialog = AnimatedProgressDialog(context, R.style.DialogThemeDimmed)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            animatedProgressDialog.show()
        } else {
            animatedProgressDialog.hide()
        }
    }
}