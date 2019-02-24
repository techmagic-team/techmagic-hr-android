package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.ui.manager.quotes.QuotesManager
import co.techmagic.hr.presentation.util.TOOLBAR_DATE_FORMAT
import co.techmagic.hr.presentation.util.calendar
import co.techmagic.hr.presentation.util.toString
import com.techmagic.viper.Router
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppTimeTrackerPresenter(val timeReportRepository: TimeReportRepository,
                                val quotesManager: QuotesManager) : BasePresenter<TimeTrackerView, ITimeTrackerRouter>(), TimeTrackerPresenter {

    private val currentDate = Calendar.getInstance()

    override fun onWeekSelected(firstDayOfWeek: Calendar) {
        // TODO: implement
    }

    override fun onDateSelected(date: Calendar) {
        // TODO: implement
    }

    override fun onBindDay(day: TimeTrackerDayView, date: Calendar) {
        view?.showToolbarTitle(date.toString(TOOLBAR_DATE_FORMAT))
        // TODO: implement logic to show reports for selected day
        // day.showReports()
    }

    override fun selectDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val dateForSelect = Calendar.getInstance()
        dateForSelect.set(year, monthOfYear, dayOfMonth)
        view?.selectDay(dateForSelect)
    }

    override fun onCurrentDayClicked() {
        view?.selectDay(currentDate)
    }

    override fun onInfoClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalendarClicked() {
        router?.openDatePicker(currentDate)
    }

}