package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import co.techmagic.hr.data.entity.time_report.DeleteTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UpdateTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.device.time_tracker.tracker_service.TaskTimerState
import co.techmagic.hr.domain.interactor.TimeTrackerInteractor
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.HrAppBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.util.ISO_WITH_TIME_ZONE_DATE_FORMAT
import co.techmagic.hr.presentation.util.TimeFormatUtil
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import rx.Single

class HrAppUpdateTimReportDetailPresenter(timeReportRepository: TimeReportRepository,
                                          userReportViewModelMapper: UserReportViewModelMapper,
                                          val timeTrackerInteractor: TimeTrackerInteractor,
                                          val projectsViewModelMapper: ProjectViewModelMapper)
    : HrAppBaseTimeReportDetailPresenter<UpdateTimeReportView>(timeReportRepository, userReportViewModelMapper), UpdateTimeReportPresenter {

    var userReportForEdit: UserReportViewModel? = null

    private var isTracking: Boolean = false
        set(value) {
            if (field != value) {
                view?.setEditable(isTracking)
            }
            field = value
        }

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        loadProjectAndTask()
        view?.setDeleteReportButtonVisible(true)

        subscription = timeTrackerInteractor.subscribeOnTimeUpdates().subscribe({ taskUpdate ->
            if (userReportForEdit?.id.equals(taskUpdate.report.id)) {
                isTracking = taskUpdate.state == TaskTimerState.RUNNING
                view?.showTime(TimeFormatUtil.formatMinutesToHours(taskUpdate.report.minutes))
            }
        }, this::showError)
    }

    override fun makeSaveRequest() {
        updateReport().subscribe(this::onReportUpdated, this::showError)
    }

    override fun deleteClicked() {
        userReportForEdit?.let { report ->
            timeTrackerInteractor.isRunning()
                    .map { it.report?.id == report.id }
                    .flatMap { running ->
                        if (running) {
                            timeTrackerInteractor.stopTimer()
                        } else {
                            Single.just(userReportViewModelMapper.retransform(report))
                        }
                    }.flatMap {
                        reportRepository
                                .deleteTask(
                                        it.weekReportId,
                                        it.id,
                                        createDeleteReportRequestBody(userReportViewModelMapper.transform(it)))
                                .doOnSubscribe { view?.showProgress(true) }
                                .doOnTerminate { view?.showProgress(false) }
                                .toSingle()
                    }.subscribe({
                        router?.projectDeleted(userReportForEdit)
                    }, this::showError)
        }
    }

    override fun startTimerClicked() {
        userReportForEdit?.let { report ->
            updateReport().flatMap {
                timeTrackerInteractor.startTimer(userReportViewModelMapper.retransform(report))
                        .map { it.current }
            }.subscribe(this::onReportUpdated, this::showError)
        }
    }

    private fun loadProjectAndTask() {
        userReportForEdit?.let {
            reportRepository
                    .getTaskDetails(userReportForEdit!!.weekReportId, userReportForEdit!!.id)
                    .doOnSubscribe { view?.showProgress(true) }
                    .doOnTerminate { view?.showProgress(false) }
                    .subscribe(
                            {
                                initTaskForEdit()
                                displayInfo()
                                this.projectViewModel = projectsViewModelMapper.transform(it.project)
                                //just display task, in PATCH request null task should be skipped
                                view?.showTask(userReportForEdit?.task!!.name)
                                view?.setTaskValid(true)
                            },
                            {
                                it.message?.let { view?.showErrorMessage(it) }
                            }
                    )
        }
    }

    private fun initTaskForEdit() {
        userReportForEdit?.let {
            this.description = userReportForEdit!!.note
            this.timeInMinutes = userReportForEdit!!.minutes
        }
    }

    private fun displayInfo() {
        view?.showDescription(description)
        view?.showTime(TimeFormatUtil.formatMinutesToHours(timeInMinutes))
    }

    private fun updateReport(): Single<UserReport> {
        return reportRepository
                .updateTask(userReportForEdit!!.weekReportId, userReportForEdit!!.id, createUpdateTaskRequestBody(
                        reportDate.formatDate(ISO_WITH_TIME_ZONE_DATE_FORMAT), reportDate
                        .firstDayOfWeekDate().formatDate(), timeInMinutes, description, projectViewModel?.id,
                        projectTaskViewModel?.task?.id, userId
                ))
                .doOnSubscribe { view?.showProgress(true) }
                .doAfterTerminate { view?.showProgress(false) }
                .map { it.report }
    }

    private fun onReportUpdated(report: UserReport) {
        router?.onReportUpdated(userReportViewModelMapper.transform(report), userReportForEdit?.id)
    }

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            projectId: String?,
                                            taskId: String?,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, RATE, projectId, taskId, userId)


    private fun createDeleteReportRequestBody(userReportForDelete: UserReportViewModel) = DeleteTaskRequestBody(
            userReportForDelete.date,
            userId,
            projectViewModel!!.id
    )
}