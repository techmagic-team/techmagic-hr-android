package co.techmagic.hr.presentation.time_tracker.time_report_detail

import com.techmagic.viper.Router
import java.util.Date

interface ITimeReportDetailRouter : Router {
    fun openSelectProject(userId : String, firstDayOfWeek : Date)
    fun openSelectTask(projectId : String)
}