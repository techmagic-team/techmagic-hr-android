package co.techmagic.hr.presentation.time_tracker.time_report_detail.base

import android.support.annotation.StringRes
import co.techmagic.hr.presentation.mvp.base.ProgressableView
import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface BaseTimeReportDetailView : View, ProgressableView {
    fun setDeleteReportButtonVisible(visible : Boolean)
    fun showDate(date: String)
    fun showProject(project: String?)
    fun showTask(task: String?)
    fun showDescription(description: String)
    fun setDescriptionValid(enabled: Boolean, @StringRes errorRes : Int?)
    fun setProjectValid(isValid: Boolean)
    fun setTaskValid(isValid: Boolean)
    fun showTime(formattedTime : String)
}

interface BaseTimeReportDetailPresenter : Presenter {
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
    fun timeChanged(time : String);
    fun startTimerClicked()
    fun saveClicked()
}