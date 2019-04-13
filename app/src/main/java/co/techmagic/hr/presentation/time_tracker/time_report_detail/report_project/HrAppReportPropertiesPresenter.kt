package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.support.annotation.IntDef
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectTaskViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectViewModelMapper
import co.techmagic.hr.presentation.util.formatDate
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppReportPropertiesPresenter(val timeReportRepository: TimeReportRepository,
                                     val projectsViewModelMapper: ProjectViewModelMapper,
                                     val projectTaskViewModelMapper: ProjectTaskViewModelMapper)
    : BasePresenter<ReportPropertiesView, IReportPropertiesRouter>(), ReportPropertiesPresenter {

    @ReportProjectType
    var type = PROJECT

    var userId: String? = null

    var projectId: String? = null
    var firstDayOfWeek: Calendar? = null

    companion object {
        @IntDef()
        @Retention(AnnotationRetention.SOURCE)
        annotation class ReportProjectType

        const val PROJECT = 0
        const val TASK = 1
    }

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        when (type) {
            PROJECT -> loadProjects()
            TASK -> loadTasks()
        }
    }

    override fun onProjectClicked(projectViewModel: ProjectViewModel) {
        router?.closeWithProject(projectViewModel)
    }

    override fun onProjectTaskClicked(projectTaskViewModel: ProjectTaskViewModel) {
        router?.closeWithProjectTask(projectTaskViewModel)
    }

    private fun loadProjects() {
        timeReportRepository
                .getProjects(userId!!, firstDayOfWeek!!.formatDate())
                .map { projectsViewModelMapper.transform(it) }
                .subscribe(
                        { view?.showProperties(it) },
                        { it?.message?.let { view?.showErrorMessage(it) } }
                )
    }

    private fun loadTasks() {
        timeReportRepository
                .getProjectTasks(projectId!!)
                .map { projectTaskViewModelMapper.transform(it) }
                .subscribe(
                        { view?.showTasks(it) },
                        { it?.message?.let { view?.showErrorMessage(it) } }
                )
    }
}