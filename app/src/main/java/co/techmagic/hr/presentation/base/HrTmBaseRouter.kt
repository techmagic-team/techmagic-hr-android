package co.techmagic.hr.presentation.base

import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import com.techmagic.viper.base.BaseRouter

open class HrTmBaseRouter<T : AppCompatActivity>(activity: T) : BaseRouter<T>(activity), HrTmRouter {

    override fun showInfoDialog(@StringRes title: Int, @StringRes description: Int) {
        showInfoDialog(title, description, null)
    }

    override fun showInfoDialog(title: Int, description: Int, confirmAction: (() -> Unit)?) {
        showInfoDialog(activity.getString(title), activity.getString(description), confirmAction)
    }

    override fun showInfoDialog(title: String, @StringRes description: Int) {
        showInfoDialog(title, description, null)
    }

    override fun showInfoDialog(title: String, description: Int, confirmAction: (() -> Unit)?) {
        showInfoDialog(title, activity.getString(description), null)

    }

    override fun showInfoDialog(title: String, description: String, confirmAction: (() -> Unit)?) {
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.message_text_ok) { _, _ -> confirmAction?.invoke() }
                .setCancelable(false)
                .show()
    }
}