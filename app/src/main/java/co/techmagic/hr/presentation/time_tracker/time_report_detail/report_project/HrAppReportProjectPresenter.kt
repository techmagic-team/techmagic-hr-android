package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.support.annotation.IntDef
import com.techmagic.viper.base.BasePresenter
import java.util.Date

class HrAppReportProjectPresenter : BasePresenter<ReportProjectsView, IReportProjectsRouter>(), ReportProjectsPresenter {

    @ReportProjectType
    var type = PROJECT

    var userId: String? = null

    var projectId: String? = null
    var firstDayOfWeek: Date? = null

    companion object {
        @IntDef()
        @Retention(AnnotationRetention.SOURCE)
        annotation class ReportProjectType

        const val PROJECT = 0
        const val TASK = 1
    }



}