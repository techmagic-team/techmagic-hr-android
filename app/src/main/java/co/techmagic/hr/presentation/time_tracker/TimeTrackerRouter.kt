package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.TimeReportDetailActivity
import co.techmagic.hr.presentation.ui.activity.HomeActivity
import com.techmagic.viper.base.BaseRouter
import java.util.*

class TimeTrackerRouter(activity: HomeActivity, val fragment: TimeTrackerFragment) :
        BaseRouter<HomeActivity>(activity),
        ITimeTrackerRouter {

    override fun openDatePicker(currentDate: Calendar) {
        fragment.showDatePicker(currentDate)
    }

    override fun openCreateTimeReport(selectedDate: Calendar, minutesInDayExcludedThis: Int) {
        TimeReportDetailActivity.start(fragment, null, selectedDate, TimeTrackerFragment.REQUEST_CREATE_NEW_TASK, minutesInDayExcludedThis)
    }

    override fun openEditTimeReport(userReport: UserReportViewModel, reportDate: Calendar, minutesInDayExcludedThis: Int) {
        TimeReportDetailActivity.start(fragment, userReport, reportDate, TimeTrackerFragment.REQUEST_UPDATE_TASK, minutesInDayExcludedThis)
    }

}
