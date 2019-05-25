package co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report

import co.techmagic.hr.data.entity.time_tracker.ReportTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.HrAppBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate

class HrAppCreateTimeReportDetailPresenter(reportRepository: TimeReportRepository,
                                           userReportViewModelMapper: UserReportViewModelMapper)
    : HrAppBaseTimeReportDetailPresenter<CreateTimeReportView>(reportRepository, userReportViewModelMapper), CreateTimeReportPresenter {

    override fun validateInfo(): Boolean {
        if (!isDescriptionValid()) {
            validateDescription()
            return false
        }

        if (!isProjectValid()) {
            validateProject()
            return false
        }

        if (!isProjectTaskValid()) {
            validateProjectTask()
            return false
        }

        return true
    }

    override fun makeSaveRequest() {
        createReport()
    }

    private fun createReport() {
        reportRepository
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
                .subscribe(
                        {
                            it.report?.let { report -> router?.onReportAdded(userReportViewModelMapper.transform(report)) }
                            view?.showProgress(false)
                        },
                        {
                            view?.showProgress(false)
                            it.message?.let { view?.showErrorMessage(it) }
                        }
                )
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