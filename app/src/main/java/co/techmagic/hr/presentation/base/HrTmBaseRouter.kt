package co.techmagic.hr.presentation.base

import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import com.techmagic.viper.base.BaseRouter

open class HrTmBaseRouter<T : AppCompatActivity>(activity: T) : BaseRouter<T>(activity), HrTmRouter {

    override fun showInfoDialog(@StringRes title: Int, @StringRes description: Int) {
        AlertDialog.Builder(activity)
                .setTitle(activity.getString(title))
                .setMessage(activity.getString(description))
                .setPositiveButton(R.string.message_text_ok, null)
                .setCancelable(false)
                .show()
    }
}