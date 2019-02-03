package co.techmagic.hr.presentation.pojo

import java.util.Date

data class UserReportViewModel(
        val id: String,
        val client: String,
        val project: String,
        val task: ReportNameViewModel,
        val note: String,
        var minutes: Int,
        val isCurrentlyTracking: Boolean,
        val isLocked: Boolean,
        val weekReportId: String,
        val status: String,
        val date: Date
)