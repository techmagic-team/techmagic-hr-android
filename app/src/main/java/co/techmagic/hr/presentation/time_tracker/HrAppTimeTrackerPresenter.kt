package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.data.entity.HolidayDate
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.device.time_tracker.tracker_service.MAX_TRACKING_TIME_MINUTES
import co.techmagic.hr.device.time_tracker.tracker_service.TaskTimerState
import co.techmagic.hr.device.time_tracker.tracker_service.TaskUpdate
import co.techmagic.hr.domain.interactor.TimeTrackerInteractor
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import co.techmagic.hr.presentation.util.*
import com.techmagic.viper.base.BasePresenter
import rx.Subscription
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// TODO: use a repository through an interactor abstraction
class HrAppTimeTrackerPresenter(
        private val dateTimeProvider: DateTimeProvider,
        private val timeReportRepository: TimeReportRepository,
        private val timeTrackerInteractor: TimeTrackerInteractor,
        private val quotesManager: QuotesManager,
        private val userReportViewMadelMapper: UserReportViewModelMapper
) : BasePresenter<TimeTrackerView, ITimeTrackerRouter>(), TimeTrackerPresenter {

    private val cache: HashMap<String, MutableList<UserReportViewModel>> = HashMap(7)
    private val holidays: HashMap<String, Holiday> = HashMap()
    private val subscriptions: HashMap<String, Subscription> = HashMap(7)

    var currentDate: Calendar = dateTimeProvider.now().dateOnly()
    var selectedDate: Calendar = currentDate.copy()

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        currentDate = dateTimeProvider.now().dateOnly()
        selectedDate = currentDate.copy()
        view?.init(currentDate)
        view?.showToolbarTitle(currentDate.formatDate(TOOLBAR_DATE_FORMAT))

        subscriptions["timer"] = call(timeTrackerInteractor.subscribeOnTimeUpdates(),
                this::updateReportViewModel,
                this::showError)
    }

    override fun onViewResumed() {
        super.onViewResumed()

        call(timeTrackerInteractor.isRunning(), {
            it.report?.date?.toCalendar()?.let { date ->
                onDateSelected(date.dateOnly())
            }
        })
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        for (s in subscriptions.values) {
            s.unsubscribe()
        }
        subscriptions.clear()
    }

    override fun onWeekSelected(firstDayOfWeek: Calendar) {
        val date = firstDayOfWeek.copy()
        val selectedDayIndex = selectedDate.get(Calendar.DAY_OF_WEEK)
        date.add(Calendar.DAY_OF_WEEK, if (selectedDayIndex == Calendar.SUNDAY) 6 else selectedDayIndex - Calendar.MONDAY) //todo: refactor!
        onDateSelected(date.copy())
    }

    override fun onDateSelected(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = dateTimeProvider.now().dateOnly()
        date.set(year, monthOfYear, dayOfMonth)
        onDateSelected(date)
    }

    override fun onDateSelected(date: Calendar) {
        if (selectedDate.isSameDate(date)) return

        selectedDate = date.copy()
        view?.notifyWeekDataChanged(date.firstDayOfWeekDate())
        view?.selectWeek(date.firstDayOfWeekDate())
        view?.selectDay(date.copy())

        view?.showToolbarTitle(date.formatDate(TOOLBAR_DATE_FORMAT))
    }

    override fun onBindWeek(weekView: TimeTrackerWeekView, firstDayOfWeek: Calendar) {
        weekView.setSelectedDay(selectedDate)
        forWeek(firstDayOfWeek) { date ->
            val key = key(date)
            weekView.setTotalTime(date, totalDayMinutes(key), holidays[key])
        }
    }

    override fun onBindDay(day: TimeTrackerDayView, date: Calendar) {
        val dayReports = getCachedReports(date)
        if (dayReports != null) {
            if (dayReports.isEmpty()) {
                day.showEmptyMessage(quotesManager.getRandomFormattedQuote())
            } else {
                day.showReports(dayReports)
            }
        } else {
            day.showEmptyMessage(quotesManager.getRandomFormattedQuote())

            val user = SharedPreferencesUtil.readUser() // TODO: refactor - inject account manager instead!!!
            val firstDayOfWeek = date.firstDayOfWeekDate()
            val weekReportsKey = key(firstDayOfWeek)
            subscriptions[weekReportsKey]?.unsubscribe()
            subscriptions[weekReportsKey] = call(
                    timeReportRepository.getDayReports(user.id, firstDayOfWeek),
                    { response ->
                        cacheHolidays(response.holidays)
                        val weekReports = response.reports.map { userReportViewMadelMapper.transform(it) }
                        initWeekCache(date)
                        for (report in weekReports) {
                            val reportDate = report.date.toCalendar();
                            cache[key(reportDate)]?.add(report)
                            view?.notifyDayReportsChanged(reportDate)
                            if (runningReport?.id?.equals(report.id) == true) {
                                checkLoadedReportForTracking(report)
                            }
                        }
                        view?.notifyWeekDataChanged(firstDayOfWeek)
                    }
            )
        }
    }

    override fun onCurrentDayClicked() {
        onDateSelected(today())
    }

    override fun onInfoClicked() {
        router?.openMonthInfo(selectedDate)
    }

    override fun onCalendarClicked() {
        router?.openDatePicker(currentDate)
    }

    override fun onNewTimeReportClicked() {
        router?.openCreateTimeReport(selectedDate, totalDayMinutesExcludeCurrent(selectedDate))
    }

    override fun onEditTimeReportClicked(reportViewModel: UserReportViewModel) {
        val reportDateCalendar = reportViewModel.date.toCalendar()
        router?.openEditTimeReport(
                reportViewModel,
                reportDateCalendar,
                totalDayMinutesExcludeCurrent(reportDateCalendar, reportViewModel.minutes)
        )
    }

    override fun onTaskCreated(userReportViewModel: UserReportViewModel?) {
        userReportViewModel ?: return

        val reports = getDayReports(userReportViewModel.date)
        reports?.add(userReportViewModel)
        view?.notifyDayReportsChanged(calendar(userReportViewModel.date).dateOnly())
    }

    override fun onTaskUpdated(oldReportId: String?, userReportViewModel: UserReportViewModel?) {
        userReportViewModel ?: return

        //todo please implement better solution using findReport(id, date) method
        with(getDayReports(userReportViewModel.date)) {
            this
                    ?.filter { it.id == userReportViewModel.id || it.id == oldReportId }
                    ?.forEachIndexed { index, viewModel ->
                        this[this.indexOf(viewModel)] = userReportViewModel
                        notifyDateChanged(userReportViewModel.date)
                    }
        }
    }

    override fun onTaskDeleted(userReportViewModel: UserReportViewModel?) {
        userReportViewModel ?: return

        with(getDayReports(userReportViewModel.date)) {
            this
                    ?.find { it.id == userReportViewModel.id }
                    ?.let {
                        this.remove(it)
                        notifyDateChanged(userReportViewModel.date)
                    }
        }
    }

    override fun onTaskTimerToggled(userReportViewModel: UserReportViewModel) {
        if (userReportViewModel.isCurrentlyTracking) {
            timeTrackerInteractor.stopTimer().subscribe({ updateReportViewModel(TaskUpdate(it, TaskTimerState.RUNNING)) }, this::showError)
        } else {
            if (canStartReportTracking(userReportViewModel)) {
                router?.let {
                    if (!it.isBatteryOptimizationTurnedOn()) {
                        it.requestTurnOffBatteryOptimization()
                    } else {
                        val userReport = userReportViewMadelMapper.retransform(userReportViewModel)
                        timeTrackerInteractor.startTimer(userReport, totalDayMinutesExcludeCurrent(userReport.date.toCalendar(), userReport.minutes))
                                .subscribe({ updateReportViewModel(TaskUpdate(it.current, TaskTimerState.RUNNING)) }, this::showError)
                    }
                }

            } else {
                router?.showTooMuchTimeErrorDialog(userReportViewModel.project, userReportViewModel.task.name)
            }
        }
    }

    private fun canStartReportTracking(report: UserReportViewModel) = totalDayMinutes(report.date.toCalendar()) < MAX_TRACKING_TIME_MINUTES

    private var runningReport: UserReportViewModel? = null

    private fun updateReportViewModel(taskUpdate: TaskUpdate) {
        if (runningReport?.id.equals(taskUpdate.report.id)) {
            when (taskUpdate.state) {
                TaskTimerState.RUNNING -> {
                    updateRunnigReportTime(taskUpdate.report.minutes)
                    return
                }
                TaskTimerState.STOPPED -> runningReport = null
            }
        }

        if (taskUpdate.state == TaskTimerState.RUNNING) {
            runningReport = userReportViewMadelMapper.transform(taskUpdate.report)
        }

        findReport(taskUpdate.report)?.let { reportViewModel ->
            reportViewModel.minutes = taskUpdate.report.minutes
            reportViewModel.isCurrentlyTracking = taskUpdate.state == TaskTimerState.RUNNING
            notifyDateChanged(reportViewModel.date)
        }
    }

    /**
     * For case when user reopens screen, and one of loaded reports has been tracking
     * if currently loaded report is the same with currently running report
     * - changes new report time and tracking status.
     * Because we do not have tracking support on backend
     */
    private fun checkLoadedReportForTracking(loadedReport: UserReportViewModel) {
        runningReport?.let {
            if (it.id.equals(loadedReport.id)) {
                loadedReport.minutes = it.minutes
                loadedReport.isCurrentlyTracking = true
            }
        }
    }

    private fun updateRunnigReportTime(newTimeMinutes: Int) {
        runningReport?.let {
            if (it.minutes != newTimeMinutes) {
                it.minutes = newTimeMinutes
                findReport(it.id, it.date)?.minutes = newTimeMinutes
                notifyDateChanged(it.date)
            }
        }
    }

    private fun notifyDateChanged(date: Date) {
        view?.notifyDayReportsChanged(calendar(date).dateOnly())
    }

    private fun getCachedReports(date: Calendar) = cache[key(date)]

    private fun key(date: Calendar) = date.formatDate()

    private fun forWeek(date: Calendar, callback: (Calendar) -> Unit) {
        val firstDay = date.firstDayOfWeekDate()
        for (i in 0 until 7) {
            val d = firstDay.copy()
            d.add(Calendar.DAY_OF_WEEK, i)
            callback(d)
        }
    }

    private fun totalDayMinutesExcludeCurrent(selectedDate: Calendar, currentMinutes: Int = 0): Int {
        return totalDayMinutes(selectedDate) - currentMinutes
    }

    private fun totalDayMinutes(selectedDate: Calendar) = totalDayMinutes(key(selectedDate))

    private fun totalDayMinutes(key: String): Int {
        val list = cache[key]
        return when (list) {
            null -> 0
            else -> list.sumBy { it.minutes }
        }
    }

    private fun initWeekCache(date: Calendar) {
        forWeek(date) {
            val key = key(it)
            if (cache[key] == null) {
                cache[key] = ArrayList()
            }
        }
    }

    private fun cacheHolidays(holidays: List<HolidayDate>) {
        for (holiday in holidays) {
            val date = DateUtil.parseStringDate(holiday.date)?.toCalendar() ?: continue
            this.holidays[key(date)] = Holiday.fromString(holiday.name)
            view?.notifyWeekDataChanged(date)
        }
    }

    private fun getDayReports(reportsDate: Date) = cache[key(calendar(reportsDate))]

    private fun findReport(report: UserReport) = findReport(report.id, report.date)

    private fun findReport(id: String, date: Date) = getDayReports(date)?.first { it.id == id }
}