package co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report

import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailView

interface UpdateTimeReportView : BaseTimeReportDetailView

interface UpdateTimeReportPresenter : BaseTimeReportDetailPresenter {
    fun deleteClicked()
}
