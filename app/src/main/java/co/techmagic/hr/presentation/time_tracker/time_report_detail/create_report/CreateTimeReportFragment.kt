package co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report

import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailFragment

class CreateTimeReportFragment : BaseTimeReportDetailFragment<CreateTimeReportPresenter>(), CreateTimeReportView {
    companion object {
        fun newInstance() = CreateTimeReportFragment()
    }
}