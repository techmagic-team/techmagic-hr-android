package co.techmagic.hr.presentation.pojo

data class TimeReportViewModel(
        val id: String,
        val client: String,
        val project: String,
        val task: String,
        val note: String,
        val time: Int,
        val isCurrentlyTracking: Boolean,
        val isLocked: Boolean
)