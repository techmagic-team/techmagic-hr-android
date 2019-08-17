package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper

import co.techmagic.hr.data.entity.time_report.TaskDetailsResponse
import co.techmagic.hr.presentation.pojo.TaskDetailViewModel

class TaskDetailViewModelMapper(private val projectViewModelMapper: ProjectViewModelMapper) {
    fun transform(taskDetailsResponse: TaskDetailsResponse) = TaskDetailViewModel(
            projectViewModelMapper.transform(taskDetailsResponse.project),
            taskDetailsResponse.client.name,
            taskDetailsResponse.status,
            taskDetailsResponse.firstDayOfWeek
    )
}