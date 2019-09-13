package co.techmagic.hr.presentation.time_tracker

import co.techmagic.hr.R
import co.techmagic.hr.presentation.base.HrTmBaseRouter
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.info.TimeInfoActivity
import co.techmagic.hr.presentation.time_tracker.time_report_detail.TimeReportDetailActivity
import co.techmagic.hr.presentation.ui.activity.HomeActivity
import co.techmagic.hr.presentation.util.isIgnoreBatteryOptimizationTurnedOn
import co.techmagic.hr.presentation.util.turnOffBatteryOptimization
import java.util.*

class TimeTrackerRouter(activity: HomeActivity, val fragment: TimeTrackerFragment) :
        HrTmBaseRouter<HomeActivity>(activity),
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

    override fun openMonthInfo(selectedDate: Calendar) {
        TimeInfoActivity.start(activity, selectedDate)
    }

    override fun showTooMuchTimeErrorDialog(project: String?, task: String?) {
        showInfoDialog(
                activity.getString(R.string.tm_hr_time_tracker_fragment_too_much_time_title, project, task),
                R.string.tm_hr_time_tracker_fragment_too_much_time_description
        )
    }

    override fun isBatteryOptimizationTurnedOn(): Boolean {
        return isIgnoreBatteryOptimizationTurnedOn(activity)
    }

    override fun requestTurnOffBatteryOptimization() {
        //TODO 13/09/2019 please throw an exception in the TimeTrackerRepository and handle
        // it in the base router instead of displaying dialog in each case
        showInfoDialog(R.string.message_warning, R.string.battery_optimization_turn_off_message) {
            turnOffBatteryOptimization(activity)
        }
    }
}
