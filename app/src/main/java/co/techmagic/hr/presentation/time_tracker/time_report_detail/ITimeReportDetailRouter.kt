package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Router
import java.util.*

interface ITimeReportDetailRouter : Router {
    fun openSelectProject(userId: String, firstDayOfWeek: Calendar)
    fun openSelectTask(projectId: String)
    fun close(userReport: UserReportViewModel?, oldReportId: String? = null)
}