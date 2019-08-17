package co.techmagic.hr.presentation.base

import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import com.techmagic.viper.base.BaseRouter

open class HrTmBaseRouter<T : AppCompatActivity>(activity: T) : BaseRouter<T>(activity), HrTmRouter {

    override fun showInfoDialog(@StringRes title: Int, @StringRes description: Int) {
        showInfoDialog(activity.getString(title), activity.getString(description))
    }

    override fun showInfoDialog(title: String, @StringRes description: Int) {
        showInfoDialog(title, activity.getString(description))
    }

    override fun showInfoDialog(title: String, description: String) {
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.message_text_ok, null)
                .setCancelable(false)
                .show()
    }
}