package co.techmagic.hr.data.repository

import co.techmagic.hr.data.entity.time_tracker.*
import co.techmagic.hr.data.manager.NetworkManager
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.util.ISO_DATE_FORMAT
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import rx.Observable
import java.util.*

class TimeReportNetworkRepository(
        private val apiClient: TimeTrackerApi,
        networkManager: NetworkManager) :
        BaseNetworkRepository(networkManager), TimeReportRepository {

    override fun getMe(): Observable<UserResponse> {
        return setup(apiClient.getMe())
    }

    override fun getDayReports(userId: String, firstDayOfWeek: Calendar): Observable<UserReportsResponse> {
        // Use date format explicitly to not depend on default param value
        val dateString = firstDayOfWeek.firstDayOfWeekDate().formatDate(ISO_DATE_FORMAT)
        return setup(apiClient.getReports(userId, dateString))
    }

    override fun getProjects(userId: String, firstDayOfWeek: String): Observable<List<ProjectResponse>> {
        return setup(apiClient.getProjects(userId, firstDayOfWeek))
    }

    override fun getProjectTasks(projectId: String): Observable<List<TaskResponse>> {
        return setup(apiClient.getProjectTasks(projectId))
    }

    override fun getTaskDetails(weekId: String, reportId: String): Observable<TaskDetailsResponse> {
        return setup(apiClient.getTaskDetails(weekId, reportId))
    }

    override fun reportTask(requestBody: ReportTaskRequestBody): Observable<ReportTaskResponse> {
        return setup(apiClient.reportTask(requestBody))
    }

    override fun updateTask(weekId: String, reportId: String, body: UpdateTaskRequestBody): Observable<UpdateUserReportResponse> {
        return setup(apiClient.updateTask(weekId, reportId, body))
    }

    override fun deleteTask(weekId: String, reportId: String, body: DeleteTaskRequestBody): Observable<Void> {
        return setup(apiClient.deleteTask(weekId, reportId, body))
    }
}