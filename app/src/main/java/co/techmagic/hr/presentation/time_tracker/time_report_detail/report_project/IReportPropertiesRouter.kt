package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import com.techmagic.viper.Router

interface IReportPropertiesRouter : Router {
    fun closeWithProject(projectViewModel: ProjectViewModel)
    fun closeWithProjectTask(projectTaskViewModel: ProjectTaskViewModel)
}