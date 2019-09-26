package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailView

interface UpdateTimeReportView : BaseTimeReportDetailView {
    fun setEditable(isEditable: Boolean)
}

interface UpdateTimeReportPresenter : BaseTimeReportDetailPresenter {
    fun deleteClicked()
}
