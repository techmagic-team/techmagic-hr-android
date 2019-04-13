package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.data.entity.time_tracker.ReportTaskRequestBody
import co.techmagic.hr.data.entity.time_tracker.UpdateTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
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
    var projectViewModel: ProjectViewModel? = null
        set(value) {
            field = value
            showProject()
        }
    var projectTaskViewModel: ProjectTaskViewModel? = null
        set(value) {
            field = value
            showProjectTask()
        }

    private var companyId: String = SharedPreferencesUtil.readUser().company.id
    private var userId: String = SharedPreferencesUtil.readUser().id

    override fun onViewResumed() {
        super.onViewResumed()
        view?.showDate(getFormattedDate())
    }

    override fun changeProjectClicked() {
        router?.openSelectProject(userId, reportDate.firstDayOfWeekDate())
    }

    override fun changeTaskClicked() {
        router?.openSelectTask(projectViewModel?.id ?: "")
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

    private fun showProject() {
        projectViewModel?.title?.let { view?.showProject(it) }
    }

    private fun showProjectTask() {
        projectTaskViewModel?.task?.name?.let { view?.showTask(it) }
    }

    private fun isNewReport() = weekId == null || reportId == null

    private fun createReport(hours: Int, note: String) {
        projectViewModel ?: return
        projectTaskViewModel ?: return

        reportRepository
                .reportTask(createReportTaskRequestBody(
                        reportDate.formatDate(),
                        reportDate.firstDayOfWeekDate().formatDate(),
                        hours,
                        note,
                        projectViewModel!!.client.id,
                        companyId,
                        projectViewModel!!.id,
                        projectTaskViewModel!!.task.id,
                        userId
                ))
                .subscribe(
                        { router?.close() },
                        { it.message?.let { view?.showErrorMessage(it) } }
                )
    }

    private fun updateReport(hours: Int, note: String) {
        projectViewModel ?: return
        projectTaskViewModel ?: return

        reportRepository
                .updateTask(weekId!!, reportId!!, createUpdateTaskRequestBody(reportDate.formatDate(), reportDate
                        .firstDayOfWeekDate().formatDate(), hours, note, projectViewModel!!.id,
                        projectTaskViewModel!!.id, userId
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
                                            clientId: String,
                                            companyId: String,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = ReportTaskRequestBody(date, firstDayOfWeek, hours, note, clientId, companyId, projectId, taskId, userId)

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, projectId, taskId, userId)

    private fun getFormattedDate() = "Not implemented"

}