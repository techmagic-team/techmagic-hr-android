package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.presentation.mvp.base.ProgressableView
import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface TimeReportDetailView : View, ProgressableView {
    fun showDate(date: String)
    fun showProject(project: String)
    fun showTask(task: String)
    fun showDescription(description: String)
    fun setDescriptionValid(enabled: Boolean)
    fun setProjectValid(isValid: Boolean)
    fun setTaskValid(isValid: Boolean)
    fun showTime(formattedTime : String)
}

interface TimeReportDetailPresenter : Presenter {
    fun onVisibleToUser()
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