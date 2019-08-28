package co.techmagic.hr.presentation.time_tracker.info

import android.support.annotation.StringRes
import com.techmagic.viper.Presenter
import com.techmagic.viper.Router
import com.techmagic.viper.View

interface TimeInfoView : View {
    fun showLoading()
    fun hideLoading()
    fun showReports(reports: List<WorkingTimeInfoViewModel>)
}

interface TimeInfoPresenter : Presenter {

}

interface TimeInfoRouter : Router {

}

data class WorkingTimeInfoViewModel(
        @StringRes
        val title: Int,
        val actualMinutes: Int,
        val expectedMinutes: Int
)