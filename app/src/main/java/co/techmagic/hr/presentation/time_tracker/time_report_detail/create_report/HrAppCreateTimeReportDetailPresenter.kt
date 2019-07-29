package co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report

import co.techmagic.hr.data.entity.time_report.ReportTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.domain.interactor.TimeTrackerInteractor
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.HrAppBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectTaskViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import rx.Observable

class HrAppCreateTimeReportDetailPresenter(reportRepository: TimeReportRepository,
                                           val timeTrackerInteractor: TimeTrackerInteractor,
                                           userReportViewModelMapper: UserReportViewModelMapper,
                                           val projectsViewModelMapper: ProjectViewModelMapper,
                                           val projectTaskViewModelMapper: ProjectTaskViewModelMapper)
    : HrAppBaseTimeReportDetailPresenter<CreateTimeReportView>(reportRepository, userReportViewModelMapper), CreateTimeReportPresenter {

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        loadLastSelectedProjectWithTask()
    }

    override fun makeSaveRequest() {
        createReport().subscribe(this::onReportCreated, this::showError)
    }

    override fun startTimer() {
        createReport()
                .flatMap { timeTrackerInteractor.startTimer(it).toObservable() }
                .map { it.current }
                .subscribe(this::onReportCreated, this::showError)
    }

    private fun loadLastSelectedProjectWithTask() {
        reportRepository
                .getLastSelectedProject()
                .zipWith(reportRepository.getLastSelectedTask()) { project, task ->
                    this.projectViewModel = projectsViewModelMapper.transform(project)
                    this.projectTaskViewModel = projectTaskViewModelMapper.transform(task)
                }
                .subscribe({}, {})
    }

    private fun createReport(): Observable<UserReport> {
        return reportRepository
                .reportTask(createReportTaskRequestBody(
                        reportDate.formatDate(),
                        reportDate.firstDayOfWeekDate().formatDate(),
                        timeInMinutes,
                        description,
                        projectViewModel!!.client!!.id,
                        companyId,
                        projectViewModel!!.id,
                        projectTaskViewModel!!.task.id,
                        userId
                ))
                .doOnSubscribe { view?.showProgress(true) }
                .doAfterTerminate { view?.showProgress(false) }
                .map { it.report }
    }

    private fun onReportCreated(report: UserReport) {
        router?.onReportAdded(userReportViewModelMapper.transform(report))
    }

    private fun createReportTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            clientId: String,
                                            companyId: String,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = ReportTaskRequestBody(date, firstDayOfWeek, hours, note, RATE, clientId, companyId, projectId, taskId, userId)

}