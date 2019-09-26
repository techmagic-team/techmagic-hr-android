package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import co.techmagic.hr.R
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailFragment

class UpdateTimeReportFragment : BaseTimeReportDetailFragment<UpdateTimeReportPresenter>(), UpdateTimeReportView {

    companion object {

        fun newInstance(alreadyReportedMinutesInDayWithoutCurrentMinutes: Int): UpdateTimeReportFragment {
            val fragment = UpdateTimeReportFragment()
            fragment.arguments = createBaseBundle(alreadyReportedMinutesInDayWithoutCurrentMinutes)
            return fragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_time_report_detail_update, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_delete -> {
                presenter?.deleteClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setEditable(isEditable: Boolean) {
        arrayOf(
                flTimeReportDetailContainer,
                tvSelectedProject,
                tvTimeReportDetailProjectError,
                tvSelectedProjectTask,
                tvTimeReportDetailTaskError,
                edDescription,
                tvDescriptionError,
                btnFifteenMinutes,
                btnThirtyMinutes,
                btnOneHour,
                btnEightHours,
                edTime,
                btnIncreaseTime,
                btnReduceTime,
//                btnStartTimer,
                btnSave
        ).forEach { it.isEnabled = isEditable }

        btnStartTimer.setText(
                if (isEditable) R.string.tm_hr_time_report_detail_start_timer
                else R.string.tm_hr_time_report_detail_stop_timer
        )
    }
}