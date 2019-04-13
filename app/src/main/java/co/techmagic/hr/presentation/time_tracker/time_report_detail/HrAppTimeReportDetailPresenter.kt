package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.data.entity.time_tracker.ReportTaskRequestBody
import co.techmagic.hr.data.entity.time_tracker.UpdateTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import co.techmagic.hr.presentation.util.TimeFormatUtil
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeReportDetailPresenter(val reportRepository: TimeReportRepository, dateTimeProvider: DateTimeProvider)
    : BasePresenter<TimeReportDetailView, ITimeReportDetailRouter>(),
        TimeReportDetailPresenter {

    companion object {
        const val RATE = 12
    }

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
    private var timeInMinutes = 0
    private var description = ""

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
        this.description = description
    }

    override fun addFifteenMinutesClicked() {
        changeSelectedTime(TimeValue.FIFTEEN_MINUTES.value)
    }

    override fun addThirtyMinutesClicked() {
        changeSelectedTime(TimeValue.THIRTY_MINUTES.value)
    }

    override fun addOneHourClicked() {
        changeSelectedTime(TimeValue.ONE_HOUR.value)
    }

    override fun addEightHoursClicked() {
        changeSelectedTime(TimeValue.EIGHT_HOURS.value)
    }

    override fun increaseTimeClicked() {
        changeSelectedTime(TimeValue.FIFTEEN_MINUTES.value)
    }

    override fun reduceTimeClicked() {
        changeSelectedTime(-TimeValue.FIFTEEN_MINUTES.value)
    }

    override fun startTimerClicked() {
    }

    override fun deleteClicked() {

    }

    override fun saveClicked() {
        if (isNewReport()) {
            createReport()
        } else {
            updateReport()
        }
    }

    private fun showProject() {
        projectViewModel?.title?.let { view?.showProject(it) }
    }

    private fun showProjectTask() {
        projectTaskViewModel?.task?.name?.let { view?.showTask(it) }
    }

    private fun isNewReport() = weekId == null || reportId == null

    private fun createReport() {
        projectViewModel ?: return
        projectTaskViewModel ?: return

        reportRepository
                .reportTask(createReportTaskRequestBody(
                        reportDate.formatDate(),
                        reportDate.firstDayOfWeekDate().formatDate(),
                        timeInMinutes,
                        description,
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

    private fun updateReport() {
        projectViewModel ?: return
        projectTaskViewModel ?: return

        reportRepository
                .updateTask(weekId!!, reportId!!, createUpdateTaskRequestBody(reportDate.formatDate(), reportDate
                        .firstDayOfWeekDate().formatDate(), timeInMinutes, description, projectViewModel!!.id,
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
                                            userId: String) = ReportTaskRequestBody(date, firstDayOfWeek, hours, note, RATE, clientId, companyId, projectId, taskId, userId)

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            projectId: String,
                                            taskId: String,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, RATE, projectId, taskId, userId)

    private fun getFormattedDate() = "Not implemented"

    private fun changeSelectedTime(value: Int) {
        timeInMinutes += value
        view?.showTime(TimeFormatUtil.formatMinutesToHours(timeInMinutes))
    }
}