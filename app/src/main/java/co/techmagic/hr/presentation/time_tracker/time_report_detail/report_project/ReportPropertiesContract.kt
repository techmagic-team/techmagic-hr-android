package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.support.annotation.StringRes
import co.techmagic.hr.presentation.mvp.base.ProgressableView
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface ReportPropertiesView : View, ProgressableView {
    fun showTitle(@StringRes titleRes : Int)
    fun showProperties(props: List<ProjectViewModel>)
    fun showTasks(props: List<ProjectTaskViewModel>)
    fun showEmptyList(@StringRes type : Int)
}

interface ReportPropertiesPresenter : Presenter {
    fun onProjectClicked(projectViewModel: ProjectViewModel)
    fun onProjectTaskClicked(projectTaskViewModel: ProjectTaskViewModel)
}