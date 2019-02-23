package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeTrackerPresenter(val timeReportRepository: TimeReportRepository,
                                val quotesManager: QuotesManager): BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter {

    override fun onWeekSelected(firstDayOfWeek: Calendar) {
        // TODO: implement
    }

    override fun onDateSelected(date: Calendar) {
        // TODO: implement
    }

    override fun onBindDay(day: TimeTrackerDayView, date: Calendar) {
        // TODO: implement logic to show reports for selected day
        // day.showReports()
    }
}