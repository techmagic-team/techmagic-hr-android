package co.techmagic.hr.presentation.time_tracker.time_report_detail.base

import co.techmagic.hr.R
import co.techmagic.hr.device.time_tracker.tracker_service.MAX_TRACKING_TIME_MINUTES
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.ITimeReportDetailRouter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.TimeValue
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.util.*
import com.techmagic.viper.base.BasePresenter
import rx.Subscription
import java.util.*

abstract class HrAppBaseTimeReportDetailPresenter
<T : BaseTimeReportDetailView>(protected val reportRepository: TimeReportRepository,
                               protected val userReportViewModelMapper: UserReportViewModelMapper)
    : BasePresenter<T, ITimeReportDetailRouter>(),
        BaseTimeReportDetailPresenter {

    var alreadyReportedMinutesInDayWithoutCurrentMinutes: Int = 0

    companion object {
        const val RATE = 12 //FYI 25 MAY 2019: this value is hardcoded; I don`t now why we should send it in the request, but it is OK for now
        const val MAX_DESCRIPTION_LENGTH = 600
    }

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
            onProjectTaskChanged()
        }

    protected var subscription: Subscription? = null

    open var companyId: String = SharedPreferencesUtil.readUser().company.id
    open var userId: String = SharedPreferencesUtil.readUser().id
    open var timeInMinutes = 0
    open var description = ""

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        view?.showDate(getFormattedDate())
    }

    override fun onViewDestroyed() {
        subscription?.unsubscribe()
        subscription = null
        super.onViewDestroyed()
    }

    override fun onVisibleToUser() {
        view?.showDate(getFormattedDate())
    }

    override fun changeProjectClicked() {
        router?.openSelectProject(userId, reportDate.firstDayOfWeekDate())
    }

    override fun changeTaskClicked() {
        if (projectViewModel != null) {
            router?.openSelectTask(projectViewModel?.id ?: "")
        } else {
            view?.setProjectValid(false)
        }
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

    override fun timeChanged(time: String) {
        timeInMinutes = TimeFormatUtil.textToMinutes(time)
    }

    final override fun startTimerClicked() {
        if (validateInfo()) {
            if (isTimeForTrackingValid()) {
                startTimer()
            } else {
                router?.showTooManyHoursForTrackingErrorDialog(getProjectTitle(), getProjectTaskTitle())
            }
        }
    }

    final override fun saveClicked() {
        saveReport()
    }

    protected open fun onProjectTaskChanged() {
        if (projectTaskViewModel != null) {
            validateProjectTask()
        }
        showProjectTask()
    }

    protected open fun askToConfirmCloseWithoutSaving() {
        router?.showYesNoDialog(
                R.string.message_warning,
                R.string.tm_hr_time_report_detail_warning_close_without_saving,
                { router?.close() }
        )
    }

    private fun validateInfo(): Boolean {
        if (!isProjectValid()) {
            validateProject()
            return false
        }

        if (!isProjectTaskValid()) {
            validateProjectTask()
            return false
        }

        if (!isDescriptionValid()) {
            validateDescription()
            return false
        }

        if (!isTimeValid()) {
            showTooManyHoursError()
            return false
        }

        return true
    }

    private fun saveReport() {
        if (validateInfo()) {
            makeSaveRequest()
        }
    }

    protected abstract fun makeSaveRequest()

    protected abstract fun startTimer()

    protected abstract fun getProjectTitle(): String?

    protected abstract fun getProjectTaskTitle(): String?

    private fun showProject() {
        view?.showProject(projectViewModel?.title ?: null)
    }

    private fun showProjectTask() {
        view?.showTask(projectTaskViewModel?.task?.name)
    }

    private fun getFormattedDate() = reportDate.formatDate(TOOLBAR_DATE_FORMAT)

    private fun changeSelectedTime(value: Int) {
        timeInMinutes += value
        if (timeInMinutes < 0) {
            timeInMinutes = 0
        }

        view?.showTime(TimeFormatUtil.formatMinutesToHours(timeInMinutes))
    }

    protected fun validateDescription() {
        val errorRes = when {
            isDescriptionLengthLongerThanMax() -> R.string.tm_hr_time_report_detail_description_tool_long_error
            isDescriptionEmpty() -> R.string.tm_hr_time_report_detail_description_empty_error
            else -> null
        }

        view?.setDescriptionValid(isDescriptionValid(), errorRes)
    }

    protected fun validateProject() = view?.setProjectValid(isProjectValid())
    protected fun validateProjectTask() = view?.setTaskValid(isProjectTaskValid())
    protected fun showTooManyHoursError() = router?.showTooManyHoursErrorDialog(
            TimeFormatUtil.formatMinutesToHours(
                    TimeFormatUtil.MAX_INPUT_MINUTES_IN_DAY - alreadyReportedMinutesInDayWithoutCurrentMinutes
            )
    )

    protected fun isDescriptionValid() = !isDescriptionEmpty() && !isDescriptionLengthLongerThanMax()
    protected fun isProjectValid() = projectViewModel != null
    protected open fun isProjectTaskValid() = projectTaskViewModel != null
    protected fun isTimeValid() = alreadyReportedMinutesInDayWithoutCurrentMinutes + timeInMinutes < TimeFormatUtil.MINUTES_IN_DAY

    protected fun isTimeForTrackingValid() = alreadyReportedMinutesInDayWithoutCurrentMinutes + timeInMinutes < MAX_TRACKING_TIME_MINUTES

    protected fun isDescriptionEmpty() = description.trim().isEmpty()
    protected fun isDescriptionLengthLongerThanMax() = description.length > MAX_DESCRIPTION_LENGTH
}