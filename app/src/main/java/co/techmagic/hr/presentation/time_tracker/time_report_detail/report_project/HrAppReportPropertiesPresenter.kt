package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import co.techmagic.hr.R
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
        @Retention(AnnotationRetention.SOURCE)
        annotation class ReportProjectType

        const val PROJECT = 0
        const val TASK = 1
    }

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        when (type) {
            PROJECT -> {
                view?.showTitle(R.string.tm_hr_report_select_project)
                loadProjects()
            }
            TASK -> {
                view?.showTitle(R.string.tm_hr_report_select_task)
                loadTasks()
            }
        }
    }

    override fun onProjectClicked(projectViewModel: ProjectViewModel) {
        timeReportRepository
                .changeLastSelectedProject(projectsViewModelMapper.reTransform(projectViewModel))
                .subscribe(
                        { router?.closeWithProject(projectViewModel) },
                        { view?.showErrorMessage(it.message ?: "") }
                )

    }

    override fun onProjectTaskClicked(projectTaskViewModel: ProjectTaskViewModel) {
        timeReportRepository
                .changeLastSelectedTask(projectTaskViewModelMapper.reTransform(projectTaskViewModel))
                .subscribe(
                        { router?.closeWithProjectTask(projectTaskViewModel) },
                        { view?.showErrorMessage(it.message ?: "") }
                )
    }

    private fun loadProjects() {
        timeReportRepository
                .getProjects(userId!!, firstDayOfWeek!!.formatDate())
                .doOnSubscribe { view?.showProgress(true) }
                .map { projectsViewModelMapper.transform(it) }
                .subscribe(
                        {
                            view?.showProperties(it)
                            view?.showProgress(false)
                        },
                        {
                            it?.message?.let { view?.showErrorMessage(it) }
                            view?.showProgress(false)
                        }
                )
    }

    private fun loadTasks() {
        timeReportRepository
                .getProjectTasks(projectId!!)
                .doOnSubscribe { view?.showProgress(true) }
                .map { projectTaskViewModelMapper.transform(it) }
                .subscribe(
                        {
                            view?.showTasks(it)
                            view?.showProgress(false)
                        },
                        {
                            it?.message?.let { view?.showErrorMessage(it) }
                            view?.showProgress(false)
                        }
                )
    }
}