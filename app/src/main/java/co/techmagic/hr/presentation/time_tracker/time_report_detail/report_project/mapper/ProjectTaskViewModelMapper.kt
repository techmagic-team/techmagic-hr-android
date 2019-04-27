package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper

import co.techmagic.hr.data.entity.time_tracker.TaskResponse
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel

class ProjectTaskViewModelMapper {

    fun transform(taskResponses: List<TaskResponse>) = taskResponses.map { transform(it) }

    fun transform(taskResponse: TaskResponse) = ProjectTaskViewModel(taskResponse.id, taskResponse.task)
}