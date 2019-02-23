package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.Presenter
import com.techmagic.viper.View
import java.util.*

interface TimeTrackerView : View {
    fun selectDay(date: Calendar)
    fun showReports(reports: List<UserReportViewModel>, date: Calendar)
    fun showEmptyMessage(quote: String, date: Calendar)
}

interface TimeTrackerPresenter : Presenter {
    fun onWeekSelected(firstDayOfWeek: Calendar)
    fun onDateSelected(date: Calendar)
}