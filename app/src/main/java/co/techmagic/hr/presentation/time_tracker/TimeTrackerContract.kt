package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.data.entity.HolidayDate
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View
import java.util.*

interface TimeTrackerView : View {
    fun init(today: Calendar)
    fun selectDay(date: Calendar)
    fun notifyDayReportsChanged(date: Calendar)
}

interface TimeTrackerWeekView {
    fun setSelectedDay(selectedDate: Calendar, isToday: Boolean)
    fun setHolidays(holidays: List<HolidayDate>)
}

interface TimeTrackerDayView {
    fun showReports(reports: List<UserReportViewModel>)
    fun showEmptyMessage(quote: String)
}

interface TimeTrackerPresenter : Presenter {
    fun onWeekSelected(firstDayOfWeek: Calendar)
    fun onDateSelected(date: Calendar)
    fun onBindWeek(weekView: TimeTrackerWeekView, firstDayOfWeek: Calendar)
    fun onBindDay(dayView: TimeTrackerDayView, date: Calendar)
}