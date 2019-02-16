package co.techmagic.hr.presentation.time_tracker

import com.techmagic.viper.Presenter
import java.util.*


interface TimeTrackerPresenter : Presenter {
    fun onWeekSelected(firstDayOfWeek: Calendar)
    fun onDateSelected(date: Calendar)
}