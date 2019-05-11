package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View
import java.util.*

interface TimeTrackerView : View {
    fun init(today: Calendar)
    fun showToolbarTitle(title: String)
    fun selectWeek(date: Calendar)
    fun selectDay(date: Calendar)
    fun notifyWeekDataChanged(date: Calendar)
    fun notifyDayReportsChanged(date: Calendar)
    fun notifyDayReportRemoved(date: Calendar)
    fun showMessage(message: String)
}

interface TimeTrackerWeekView {
    fun setSelectedDay(selectedDate: Calendar)
    fun setTotalTime(date: Calendar, minutes: Int, holiday: Holiday?)
}

interface TimeTrackerDayView {
    fun showReports(reports: List<UserReportViewModel>)
    fun showEmptyMessage(quote: String)
}

interface TimeTrackerPresenter : Presenter {
    fun onWeekSelected(firstDayOfWeek: Calendar)
    fun onDateSelected(date: Calendar)
    fun onDateSelected(year: Int, monthOfYear: Int, dayOfMonth: Int)

    fun onBindWeek(weekView: TimeTrackerWeekView, firstDayOfWeek: Calendar)
    fun onBindDay(dayView: TimeTrackerDayView, date: Calendar)

    fun onCurrentDayClicked()
    fun onInfoClicked()
    fun onCalendarClicked()

    fun onNewTimeReportClicked()

    fun onEditTimeReportClicked(reportViewModel: UserReportViewModel)

    fun onTaskCreated(userReportViewModel: UserReportViewModel?)
    fun onTaskUpdated(oldReportId : String?, userReportViewModel: UserReportViewModel?)
    fun onTaskDeleted(userReportViewModel : UserReportViewModel?)
}