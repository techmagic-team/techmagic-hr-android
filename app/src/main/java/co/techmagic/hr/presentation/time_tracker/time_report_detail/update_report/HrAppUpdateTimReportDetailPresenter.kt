package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import co.techmagic.hr.data.entity.time_tracker.UpdateTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.time_report_detail.HrAppBaseBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectTaskViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectViewModelMapper
import co.techmagic.hr.presentation.util.*

class HrAppUpdateTimReportDetailPresenter(timeReportRepository: TimeReportRepository,
                                          val projectsViewModelMapper: ProjectViewModelMapper,
                                          val projectTaskViewModelMapper: ProjectTaskViewModelMapper)
    : HrAppBaseBaseTimeReportDetailPresenter(timeReportRepository) {

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        loadProjectAndTask()
    }

    override fun validateInfo(): Boolean {
        if (!isDescriptionValid()) {
            validateDescription()
            return false
        }

        if (isProjectReSelected() && !isProjectTaskValid()) {
            validateProjectTask()
            return false
        }

        return true
    }

    override fun makeRequest() {
        updateReport()
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

    private fun isProjectReSelected() = userReportForEdit?.id?.equals(projectViewModel?.id) ?: true

    private fun updateReport() {
        reportRepository
                .updateTask(userReportForEdit!!.weekReportId, userReportForEdit!!.id, createUpdateTaskRequestBody(
                        reportDate.formatDate(ISO_WITH_TIME_ZONE_DATE_FORMAT), reportDate
                        .firstDayOfWeekDate().formatDate(), timeInMinutes, description, projectViewModel?.id,
                        projectTaskViewModel?.task?.id, userId
                ))
                .doOnSubscribe { view?.showProgress(true) }
                .doOnTerminate { view?.showProgress(false) }
                .subscribe(
                        { router?.close() },
                        { it.message?.let { view?.showErrorMessage(it) } }
                )
    }

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            projectId: String?,
                                            taskId: String?,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, RATE, projectId, taskId, userId)

}