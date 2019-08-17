package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Router
import java.util.*

interface ITimeTrackerRouter : Router {
    fun openDatePicker(currentDate: Calendar)
    fun openCreateTimeReport(selectedDate : Calendar, minutesInDayExcludedThis: Int)
    fun openEditTimeReport(userReport: UserReportViewModel, reportDate: Calendar, minutesInDayExcludedThis: Int)
    fun openMonthInfo(selectedDate : Calendar)
}