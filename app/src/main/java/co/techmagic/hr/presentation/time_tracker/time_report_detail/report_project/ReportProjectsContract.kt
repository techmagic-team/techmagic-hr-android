package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.TaskViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface ReportProjectsView : View {
    fun showProperties(props: List<ProjectViewModel>)
    fun showTasks(props: List<TaskViewModel>)
}

interface ReportProjectsPresenter : Presenter {

}