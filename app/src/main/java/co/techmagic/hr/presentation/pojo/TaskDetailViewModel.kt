package co.techmagic.hr.presentation.pojo

import java.util.*

data class TaskDetailViewModel(
        val projectViewModel: ProjectViewModel,
        val clientName: String,
        val status: String,
        val firstDayOfWeek: Date
)