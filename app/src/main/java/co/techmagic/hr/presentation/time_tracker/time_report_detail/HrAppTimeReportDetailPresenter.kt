package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.data.entity.time_tracker.ReportTaskRequestBody
import co.techmagic.hr.data.entity.time_tracker.UpdateTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeReportDetailPresenter(val reportRepository: TimeReportRepository, dateTimeProvider: DateTimeProvider)
    : BasePresenter<TimeReportDetailView, ITimeReportDetailRouter>(),
        TimeReportDetailPresenter {


    var weekId: String? = null
    var reportId: String? = null
    lateinit var reportDate: Calendar

/*------------------------------------------*/


    private var rate = 12 //todo I don`t know where I can get this value
    private var clientId: String = "5ae1e7bd06fdcf255ec09373"//todo get from task response
    private var companyId: String = "58133d8488dbe7d5b0bfb745" //todo get from task response
    private var projectId: String = "5b7d9309e8ef8c6d6759b919"//todo get from task response
    private var taskId: String = "5ae1e32006fdcf255ec0885c" //todo get from task response
    private var userId: String = "5c02b40ca8ed0759deba2344"//todo get from bundle or from request


    override fun onViewResumed() {
        super.onViewResumed()
        view?.showDate(getFormattedDate())
    }

    override fun changeProjectClicked() {
        router?.openSelectProject(userId, reportDate.firstDayOfWeekDate())
    }

    override fun changeTaskClicked() {
        router?.openSelectTask(projectId)
    }

    override fun descriptionChanged(description: String) {
    }

    override fun addFifteenMinutesClicked() {
    }

    override fun addThirtyMinutesClicked() {
    }

    override fun addOneHourClicked() {
    }

    override fun addEightHoursClicked() {
    }

    override fun increaseTimeClicked() {
    }

    override fun reduceTimeClicked() {
    }

    override fun startTimerClicked() {
    }

    override fun deleteClicked() {

    }

    override fun saveClicked(hours: Int, note: String) {
        if (isNewReport()) {
            createReport(hours, note)
        } else {
            updateReport(hours, note)
        }
    }

    private fun isNewReport() = weekId == null || reportId == null

    private fun createReport(hours: Int, note: String) {
        reportRepository
                .reportTask(createReportTaskRequestBody(
                        reportDate.formatDate(),
                        reportDate.firstDayOfWeekDate().formatDate(),
                        hours,
                        note,
                        rate,
                        clientId,
                        companyId,
                        projectId,
                        taskId,
                        userId
                ))
                .subscribe(
                        { router?.close() },
                        { it.message?.let { view?.showErrorMessage(it) } }
                )
    }

    private fun updateReport(hours: Int, note: String) {
        reportRepository
                .updateTask(weekId!!, reportId!!, createUpdateTaskRequestBody(reportDate.formatDate(), reportDate
                        .firstDayOfWeekDate().formatDate(), hours, note, rate, projectId, taskId, userId
                ))
                .subscribe(
                        { router?.close() },
                        { it.message?.let { view?.showErrorMessage(it) } }
                )
    }

    private fun createReportTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            rate: Int,
                                            clientId: String,
                                            companyId: String,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = ReportTaskRequestBody(date, firstDayOfWeek, hours, note, rate, clientId, companyId, projectId, taskId, userId)

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            rate: Int,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, rate, projectId, taskId, userId)

    private fun getFormattedDate() = "Not implemented"

}