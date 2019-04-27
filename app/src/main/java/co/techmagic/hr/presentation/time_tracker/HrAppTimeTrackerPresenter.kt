package co.techmagic.hr.presentation.time_tracker

import android.os.Handler
import co.techmagic.hr.data.entity.HolidayDate
import co.techmagic.hr.data.entity.time_tracker.UserReport
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ReportNameViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel
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
        private val quotesManager: QuotesManager) : BasePresenter<TimeTrackerView, ITimeTrackerRouter>(), TimeTrackerPresenter {

    private val cache: HashMap<String, MutableList<UserReportViewModel>> = HashMap(7)
    private val holidays: HashMap<String, Holiday> = HashMap()
    private val subscriptions: HashMap<String, Subscription> = HashMap(7)

    var currentDate: Calendar = dateTimeProvider.now().dateOnly()
    var selectedDate: Calendar = currentDate.copy()

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        view?.init(selectedDate)
        Handler().postDelayed({ onEditTimeReportClicked(0) }, 5000)
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
            subscriptions[weekReportsKey] = timeReportRepository.getDayReports(user.id, firstDayOfWeek)
                    .subscribe { response ->
                        cacheHolidays(response.holidays)
                        val weekReports = response.reports.map { reportToViewModel(it) }
                        initWeekCache(date)
                        for (report in weekReports) {
                            cache[key(report.date.toCalendar())]?.add(report)
                            view?.notifyDayReportsChanged(date)
                        }
                        view?.notifyWeekDataChanged(firstDayOfWeek)
                    }
        }
    }

    override fun onCurrentDayClicked() {
        onDateSelected(today())
    }

    override fun onInfoClicked() {
        //TODO: implement
        view?.showMessage("Not implemented")
    }

    override fun onCalendarClicked() {
        router?.openDatePicker(currentDate)
    }

    override fun onNewTimeReportClicked() {
        router?.openCreateTimeReport()
    }

    override fun onEditTimeReportClicked(position: Int) {
        val userReportViewModel: UserReportViewModel = cache[key(today())]!![0]
        router?.openEditTimeReport(userReportViewModel, userReportViewModel.date.toCalendar())
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
            val date = DateUtil.parseStringDate(holiday.date).toCalendar()
            this.holidays[key(date)] = Holiday.fromString(holiday.name)
            view?.notifyWeekDataChanged(date)
        }
    }

    private fun reportToViewModel(report: UserReport): UserReportViewModel {
        return UserReportViewModel(
                report.id,
                report.client,
                report.project,
                ReportNameViewModel(report.task.name),
                report.note,
                report.minutes,
                false,
                report.isApproved,
                report.weekReportId,
                report.status,
                report.date
        )
    }
}