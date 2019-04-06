package co.techmagic.hr.presentation.time_tracker.time_report_detail

import co.techmagic.hr.R
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment
import com.techmagic.viper.base.BaseRouter
import java.util.*

class TimeReportDetailRouter(activity: TimeReportDetailActivity, val fragment: TimeReportDetailFragment)
    : BaseRouter<TimeReportDetailActivity>(activity), ITimeReportDetailRouter {

    override fun openSelectProject(userId: String, firstDayOfWeek: Date) {
        replaceFragment(R.id.fragment_container, ReportPropertiesFragment.newProjectsInstance(userId, firstDayOfWeek), true)
    }

    override fun openSelectTask(projectId: String) {
        replaceFragment(R.id.fragment_container, ReportPropertiesFragment.newTasksInstance(projectId), true)
    }

    override fun close() {
        activity.finish()
    }
}
