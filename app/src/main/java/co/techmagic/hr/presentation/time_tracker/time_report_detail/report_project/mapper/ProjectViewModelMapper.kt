package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper

import co.techmagic.hr.data.entity.time_report.Project
import co.techmagic.hr.data.entity.time_report.ProjectResponse
import co.techmagic.hr.presentation.pojo.ProjectViewModel

class ProjectViewModelMapper {

    fun transform(projectResponses: List<ProjectResponse>) = projectResponses.map { transform(it) }

    fun transform(projectResponse: ProjectResponse) = ProjectViewModel(
            projectResponse.id,
            projectResponse.name,
            projectResponse.client
    )

    fun transform(project: Project) = ProjectViewModel(
            project.id,
            project.name,
            null
    )

    fun reTransform(projectViewModel: ProjectViewModel) = ProjectResponse(
            projectViewModel.id,
            projectViewModel.title,
            projectViewModel.client
    )
}