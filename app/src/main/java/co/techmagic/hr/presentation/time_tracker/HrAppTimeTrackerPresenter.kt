package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.domain.repository.TimeReportRepository
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeTrackerPresenter(val timeReportRepository: TimeReportRepository): BasePresenter<TimeTrackerView, Router>(), TimeTrackerPresenter {

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