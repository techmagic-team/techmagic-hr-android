import co.techmagic.hr.presentation.pojo.UserReportViewModel
import com.techmagic.viper.View

interface TimeTrackerView : View {
    fun showReports(reports: List<UserReportViewModel>)

    fun showEmptyMessage(quote: String)
}