package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Router
import java.util.*

interface ITimeReportDetailRouter : Router {
    fun openSelectProject(userId: String, firstDayOfWeek: Calendar)
    fun openSelectTask(projectId: String)
    fun onReportAdded(userReport: UserReportViewModel?)
    fun onReportUpdated(userReport: UserReportViewModel?, oldReportId: String? = null)
    fun projectDeleted(deletedReport: UserReportViewModel?)
}