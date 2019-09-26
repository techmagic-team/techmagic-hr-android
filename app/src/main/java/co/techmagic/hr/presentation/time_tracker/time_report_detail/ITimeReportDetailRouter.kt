package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.support.annotation.StringRes
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Router
import java.util.*

interface ITimeReportDetailRouter : Router {
    fun openSelectProject(userId: String, firstDayOfWeek: Calendar)
    fun openSelectTask(projectId: String)
    fun onReportAdded(userReport: UserReportViewModel?)
    fun onReportUpdated(userReport: UserReportViewModel?, oldReportId: String? = null)
    fun projectDeleted(deletedReport: UserReportViewModel?)
    fun showYesNoDialog(@StringRes titleRes: Int, @StringRes messageRes: Int, onConfirm: () -> Unit, onCancel: () -> Unit = {})
    fun showTooManyHoursErrorDialog(timeLeft : String)
    fun showTooManyHoursForTrackingErrorDialog(project : String?, task : String?)
    fun isBatteryOptimizationTurnedOn() : Boolean
    fun requestTurnOffBatteryOptimization()
    fun close()
}