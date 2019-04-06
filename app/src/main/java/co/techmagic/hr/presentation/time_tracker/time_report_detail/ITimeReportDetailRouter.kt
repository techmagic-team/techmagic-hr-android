package co.techmagic.hr.presentation.time_tracker.time_report_detail

import com.techmagic.viper.Router
import java.util.*

interface ITimeReportDetailRouter : Router {
    fun openSelectProject(userId : String, firstDayOfWeek : Calendar)
    fun openSelectTask(projectId : String)
    fun close()
}