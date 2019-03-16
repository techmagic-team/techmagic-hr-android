package co.techmagic.hr.presentation.time_tracker

import com.techmagic.viper.Router
import java.util.*

interface ITimeTrackerRouter : Router {
    fun openDatePicker(currentDate: Calendar)
}