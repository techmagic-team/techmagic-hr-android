package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.View

interface TimeTrackerView : View {
    fun showReports(reports: List<UserReportViewModel>)
}