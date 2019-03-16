package co.techmagic.hr.presentation.time_tracker.time_report_detail

import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface TimeReportDetailView : View {
    fun showProject(project: String)
    fun showTask(task: String)
    fun showDescription(description: String)
    fun setEditingEnabled(enabled: Boolean)
}

interface TimeReportDetailPresenter : Presenter {
    fun changeProjectclicked()
    fun changeTaskClicked()
    fun addFifteenMinutesClicked()
    fun addThirtyMinutesclicked()
    fun addOneHourClicked()
    fun addEigthHoursClicked()
    fun reduceTimeClicked()
    fun startTimerClicked()
    fun saveClicked()
}