package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import co.techmagic.hr.R
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailFragment

class UpdateTimeReportFragment : BaseTimeReportDetailFragment<UpdateTimeReportPresenter>(), UpdateTimeReportView {

    companion object {
        fun newInstance() = UpdateTimeReportFragment()
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
}