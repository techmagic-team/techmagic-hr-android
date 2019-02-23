package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import co.techmagic.hr.presentation.util.copy
import co.techmagic.hr.presentation.util.dateOnly
import co.techmagic.hr.presentation.util.isSameDate
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeTrackerPresenter(
        private val dateTimeProvider: DateTimeProvider,
        private val timeReportRepository: TimeReportRepository,
        private val quotesManager: QuotesManager) : BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter {

    var selectedDate: Calendar = dateTimeProvider.now().dateOnly()

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        view?.init(selectedDate)
    }

    override fun onWeekSelected(firstDayOfWeek: Calendar) {
        // TODO: implement
    }

    override fun onDateSelected(date: Calendar) {
        // TODO: implement
        selectedDate = date.copy()
    }

    override fun onBindWeek(weekView: TimeTrackerWeekView, firstDayOfWeek: Calendar) {
        weekView.setSelectedDay(selectedDate, dateTimeProvider.now().isSameDate(selectedDate))
        // todo: set holidays
    }

    override fun onBindDay(day: TimeTrackerDayView, date: Calendar) {
        // TODO: implement logic to show reports for selected day
    }
}