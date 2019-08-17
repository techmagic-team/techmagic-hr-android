package co.techmagic.hr.presentation.time_tracker.info

import com.techmagic.viper.Presenter
import com.techmagic.viper.Router
import com.techmagic.viper.View

interface TimeInfoView : View {
    fun showLoading()
    fun hideLoading()
    fun showReports(reports: List<TimeReportViewModel>)
}

interface TimeInfoPresenter : Presenter {

}

interface TimeInfoRouter : Router {

}

data class TimeReportViewModel(
        val title: String,
        val actualMinutes: Int,
        val expectedMinutes: Int
)