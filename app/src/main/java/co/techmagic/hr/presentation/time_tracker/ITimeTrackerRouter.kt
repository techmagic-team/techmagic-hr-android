package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Router
import java.util.*

interface ITimeTrackerRouter : Router {
    fun openDatePicker(currentDate: Calendar)
    fun openCreateTimeReport(selectedDate: Calendar, hoursInDayExcludedThis: Int)
    fun openEditTimeReport(userReport: UserReportViewModel, reportDate: Calendar, hoursInDayExcludedThis: Int)
    fun openMonthInfo(selectedDate : Calendar)
    fun showTooMuchTimeErrorDialog(project : String?, task : String?)
}