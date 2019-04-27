package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.data.entity.time_tracker.UpdateTaskRequestBody
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.util.*
import com.techmagic.viper.base.BasePresenter
import java.util.*

abstract class HrAppBaseBaseTimeReportDetailPresenter(val reportRepository: TimeReportRepository)
    : BasePresenter<TimeReportDetailView, ITimeReportDetailRouter>(),
        BaseTimeReportDetailPresenter {

    companion object {
        const val RATE = 12
    }

    var userReportForEdit: UserReportViewModel? = null

    lateinit var reportDate: Calendar
    var projectViewModel: ProjectViewModel? = null
        set(value) {
            field = value
            projectTaskViewModel = null
            validateProject()
            showProject()
        }
    var projectTaskViewModel: ProjectTaskViewModel? = null
        set(value) {
            field = value
            validateProjectTask()
            showProjectTask()
        }

    open var companyId: String = SharedPreferencesUtil.readUser().company.id
    open var userId: String = SharedPreferencesUtil.readUser().id
    open var timeInMinutes = 0
    open var description = ""

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        view?.showDate(getFormattedDate())
    }

    override fun onVisibleToUser() {
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
        validateDescription()
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
        if (!validateInfo()) {
            return
        }
        makeRequest()
    }

    abstract fun makeRequest()

    private fun showProject() {
        projectViewModel?.title?.let { view?.showProject(it) }
    }

    private fun showProjectTask() {
        projectTaskViewModel?.task?.name?.let { view?.showTask(it) }
    }

    private fun isNewReport() = userReportForEdit == null

    private fun getFormattedDate() = reportDate.formatDate(TOOLBAR_DATE_FORMAT)

    private fun changeSelectedTime(value: Int) {
        timeInMinutes += value
        if (timeInMinutes < 0) {
            timeInMinutes = 0
        }

        view?.showTime(TimeFormatUtil.formatMinutesToHours(timeInMinutes))
    }

    private fun validateInfo(): Boolean {
        return validateDescription() && validateProject() && validateProjectTask()
    }

    private fun validateDescription(): Boolean {
        val isDescriptionValid = !description.isEmpty()
        view?.setDescriptionValid(isDescriptionValid)
        return isDescriptionValid
    }

    private fun validateProject(): Boolean {
        val isProjectValid = projectViewModel != null
        view?.setProjectValid(isProjectValid)
        return isProjectValid
    }

    private fun validateProjectTask(): Boolean {
        val isTaskValid = projectTaskViewModel != null
        view?.setTaskValid(isTaskValid)
        return isTaskValid
    }
}