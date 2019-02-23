package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View
import java.util.*

interface TimeTrackerView : View {
    fun selectDay(date: Calendar)

    fun showToolbarTitle(title : String)
}

interface TimeTrackerDayView {
    fun showReports(reports: List<UserReportViewModel>)
    fun showEmptyMessage(quote: String)
}

interface TimeTrackerPresenter : Presenter {
    fun onWeekSelected(firstDayOfWeek: Calendar)
    fun onDateSelected(date: Calendar)
    fun onBindDay(day: TimeTrackerDayView, date: Calendar)
    fun onCurrentDayClicked()
    fun onInfoClicked()
    fun onCalendarClicked()
}