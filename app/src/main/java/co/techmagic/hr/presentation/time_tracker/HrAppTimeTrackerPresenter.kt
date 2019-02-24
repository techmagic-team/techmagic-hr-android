package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.data.entity.time_tracker.UserReport
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.pojo.ReportNameViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import co.techmagic.hr.presentation.util.*
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter
import rx.Subscription
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HrAppTimeTrackerPresenter(
        private val dateTimeProvider: DateTimeProvider,
        private val timeReportRepository: TimeReportRepository,
        private val quotesManager: QuotesManager) : BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter {

    private val cache: HashMap<String, MutableList<UserReportViewModel>> = HashMap(7)
    private val subscriptions: HashMap<String, Subscription> = HashMap(7)

    var selectedDate: Calendar = dateTimeProvider.now().dateOnly()

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        view?.init(selectedDate)
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        for (s in subscriptions.values) {
            s.unsubscribe()
        }
        subscriptions.clear()
    }

    override fun onWeekSelected(firstDayOfWeek: Calendar, selectedDayIndex: Int) {
        val date = firstDayOfWeek.copy()
        date.add(Calendar.DAY_OF_WEEK, selectedDayIndex)
        onDateSelected(date.copy())
    }

    override fun onDateSelected(date: Calendar) {
        if (selectedDate.isSameDate(date)) return

        selectedDate = date.copy()
        view?.notifyWeekDataChanged(date.firstDayOfWeekDate())
        view?.selectWeek(date.firstDayOfWeekDate())
        view?.selectDay(date.copy())
    }

    override fun onBindWeek(weekView: TimeTrackerWeekView, firstDayOfWeek: Calendar) {
        weekView.setSelectedDay(selectedDate, dateTimeProvider.now().isSameDate(selectedDate))
        // todo: set holidays
    }

    override fun onBindDay(day: TimeTrackerDayView, date: Calendar) {
        val key = key(date)
        val reports = cache[key]
        if (reports != null) {
            if (reports.isEmpty()) {
                day.showEmptyMessage(quotesManager.getRandomFormatedQuote())
            } else {
                day.showReports(reports)
            }
        } else {
            day.showEmptyMessage(quotesManager.getRandomFormatedQuote())

            val user = SharedPreferencesUtil.readUser() // TODO: refactor - inject account manager instead!!!
            subscriptions[key]?.unsubscribe()
            subscriptions[key] = timeReportRepository.getDayReports(user.id, date.firstDayOfWeekDate())
                    .subscribe { response ->
                        val weekReports = response.reports.map { reportToViewModel(it) }
                        initWeekCache(date)
                        for (report in weekReports) {
                            cache[key(report.date.toCalendar())]?.add(report)
                            view?.notifyDayReportsChanged(date)
                        }
                    }
        }
    }

    private fun key(date: Calendar) = date.formatDate()

    private fun initWeekCache(date: Calendar) {
        val firstDay = date.firstDayOfWeekDate()
        for (i in 0 until 7) {
            val date = firstDay.copy()
            date.add(Calendar.DAY_OF_WEEK, i)
            val key = key(date)
            if (cache[key] == null) {
                cache[key] = ArrayList()
            }
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
                report.lockDate,
                report.weekReportId,
                report.status,
                report.date
        )
    }
}