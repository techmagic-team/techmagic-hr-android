package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import android.util.Log
import co.techmagic.hr.data.entity.time_report.DeleteTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UpdateTaskRequestBody
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

class HrAppUpdateTimReportDetailPresenter(timeReportRepository: TimeReportRepository,
                                          userReportViewModelMapper: UserReportViewModelMapper,
                                          val timeTrackerInteractor: TimeTrackerInteractor,
                                          val projectsViewModelMapper: ProjectViewModelMapper)
    : HrAppBaseTimeReportDetailPresenter<UpdateTimeReportView>(timeReportRepository, userReportViewModelMapper), UpdateTimeReportPresenter {

    var userReportForEdit: UserReportViewModel? = null

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        loadProjectAndTask()
        view?.setDeleteReportButtonVisible(true)
    }

    override fun makeSaveRequest() {
        updateReport()
    }

    override fun deleteClicked() {
        userReportForEdit?.let { report ->
            reportRepository
                    .deleteTask(
                            report.weekReportId,
                            report.id,
                            createDeleteReportRequestBody(report))
                    .doOnSubscribe { view?.showProgress(true) }
                    .doOnTerminate { view?.showProgress(false) }
                    .subscribe({
                        //                        timeTrackerInteractor.discardTimer() //todo: discard timer if needed
                        router?.projectDeleted(userReportForEdit)
                    }, { error ->
                        error?.message?.let { view?.showErrorMessage(it) }
                    })
        }
    }

    override fun startTimerClicked() {
        userReportForEdit?.let { report ->
            timeTrackerInteractor
                    .startTimer(userReportViewModelMapper.retransform(report))
                    .subscribe {
                        Log.d("TEST_TIMER", "OnComplete from start timer")

                        timeTrackerInteractor.subscribeOnTimeUpdates()
                                .subscribe({
                                    Log.d("TEST_TIMER", "Update in presenter $it")
                                }, {
                                    Log.d("TEST_TIMER", "Update in presenter error")
                                }, {
                                    Log.d("TEST_TIMER", "Update in presenter complete")
                                })
                    }
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

    private fun updateReport() {
        reportRepository
                .updateTask(userReportForEdit!!.weekReportId, userReportForEdit!!.id, createUpdateTaskRequestBody(
                        reportDate.formatDate(ISO_WITH_TIME_ZONE_DATE_FORMAT), reportDate
                        .firstDayOfWeekDate().formatDate(), timeInMinutes, description, projectViewModel?.id,
                        projectTaskViewModel?.task?.id, userId
                ))
                .doOnSubscribe { view?.showProgress(true) }
                .subscribe(
                        {
                            view?.showProgress(false)
                            it.report?.let { report -> router?.onReportUpdated(userReportViewModelMapper.transform(report), userReportForEdit?.id) }
                        },
                        {
                            view?.showProgress(false)
                            it.message?.let { view?.showErrorMessage(it) }
                            it?.printStackTrace()
                        }
                )
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