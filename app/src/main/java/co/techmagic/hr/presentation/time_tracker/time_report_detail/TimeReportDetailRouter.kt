package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.app.Activity
import android.content.Intent
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment
import com.techmagic.viper.base.BaseRouter
import java.util.*

class TimeReportDetailRouter(activity: TimeReportDetailActivity, val fragment: TimeReportDetailFragment)
    : BaseRouter<TimeReportDetailActivity>(activity), ITimeReportDetailRouter {

    override fun openSelectProject(userId: String, firstDayOfWeek: Calendar) {
        addFragment(R.id.fragment_container, ReportPropertiesFragment.newProjectsInstance(userId, firstDayOfWeek), true)
    }

    override fun openSelectTask(projectId: String) {
        addFragment(R.id.fragment_container, ReportPropertiesFragment.newTasksInstance(projectId), true)
    }

    override fun close(userReport: UserReportViewModel?) {
        val intent = Intent()
        intent.putExtra(TimeReportDetailActivity.EXTRA_USER_REPORT, userReport)
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }
}
