package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.app.Activity
import android.content.Intent
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.TimeTrackerFragment
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

    override fun projectDeleted(deletedReport: UserReportViewModel?) {
        activity.setResult(TimeTrackerFragment.RESULT_REPORT_DELETED, buildCloseIntent(deletedReport))
        activity.finish()
    }

    override fun onReportUpdated(userReport: UserReportViewModel?, oldReportId: String?) {
        val intent = buildCloseIntent(userReport)
        intent.putExtra(TimeReportDetailActivity.EXTRA_OLD_ID, oldReportId)
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    override fun onReportAdded(userReport: UserReportViewModel?) {
        activity.setResult(Activity.RESULT_OK, buildCloseIntent(userReport))
        activity.finish()
    }

    private fun buildCloseIntent(userReport: UserReportViewModel?): Intent {
        val intent = Intent()
        intent.putExtra(TimeReportDetailActivity.EXTRA_USER_REPORT, userReport)
        return intent
    }
}
