package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper

import co.techmagic.hr.data.entity.time_tracker.UserReport
import co.techmagic.hr.presentation.pojo.ReportNameViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel

class UserReportViewModelMapper {
    fun transform(report: UserReport) = UserReportViewModel(
            report.id,
            report.client,
            report.project,
            ReportNameViewModel(report.task.name),
            report.note,
            report.minutes,
            false,
            report.isApproved,
            report.weekReportId,
            report.status,
            report.date
    )
}