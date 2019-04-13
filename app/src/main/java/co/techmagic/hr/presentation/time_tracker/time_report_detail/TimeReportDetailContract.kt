package co.techmagic.hr.presentation.time_tracker.time_report_detail

import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface TimeReportDetailView : View {
    fun showDate(date: String)
    fun showProject(project: String)
    fun showTask(task: String)
    fun showDescription(description: String)
    fun setDescriptionValid(enabled: Boolean)
    fun showTime(formatedTime : String)
}

interface TimeReportDetailPresenter : Presenter {
    fun changeProjectClicked()
    fun changeTaskClicked()
    fun descriptionChanged(description: String)
    fun addFifteenMinutesClicked()
    fun addThirtyMinutesClicked()
    fun addOneHourClicked()
    fun addEightHoursClicked()
    fun increaseTimeClicked()
    fun reduceTimeClicked()
    fun startTimerClicked()
    fun deleteClicked()
    fun saveClicked()
}