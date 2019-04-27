package co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report

import co.techmagic.hr.data.entity.time_tracker.ReportTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.time_report_detail.HrAppBaseBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate

class HrAppCreateBaseTimeReportDetailPresenter(reportRepository: TimeReportRepository)
    : HrAppBaseBaseTimeReportDetailPresenter(reportRepository) {

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
    }

    override fun makeRequest() {
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
                            router?.close()
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