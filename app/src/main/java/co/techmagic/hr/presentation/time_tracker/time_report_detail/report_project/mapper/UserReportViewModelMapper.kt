package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper

import co.techmagic.hr.data.entity.time_report.ReportName
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.presentation.pojo.ReportNameViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel

class UserReportViewModelMapper {
    fun transform(report: UserReport) = UserReportViewModel(
            report.id,
            report.client,
            report.project,
            report.lockDate,
            ReportNameViewModel(report.task.name),
            report.note,
            report.minutes,
            false,
            report.isApproved || report.lockDate,
            report.weekReportId,
            report.status,
            report.date
    )

    fun retransform(report: UserReportViewModel) = UserReport(
            report.id,
            ReportName(report.task.name),
            report.minutes,
            report.date,
            report.lockDate,
            report.note,
            report.status,
            report.client,
            report.project,
            report.weekReportId
    )
}