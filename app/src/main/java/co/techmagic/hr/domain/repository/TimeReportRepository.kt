package co.techmagic.hr.domain.repository

import co.techmagic.hr.data.entity.time_tracker.*
import rx.Observable
import java.util.*

interface TimeReportRepository {
    fun getMe(): Observable<UserResponse>
    fun getDayReports(userId: String, firstDayOfWeek: Calendar): Observable<UserReportsResponse>
    fun getProjects(userId: String, firstDayOfWeek: String): Observable<List<ProjectResponse>>
    fun getProjectTasks(projectId: String): Observable<List<TaskResponse>>
    fun getTaskDetails(weekId: String, reportId: String): Observable<TaskDetailsResponse>
    fun reportTask(requestBody: ReportTaskRequestBody): Observable<ReportTaskResponse>
    fun updateTask(weekId: String, reportId: String, body: UpdateTaskRequestBody): Observable<UpdateUserReportResponse>
    fun deleteTask(weekId: String, reportId: String, body: DeleteTaskRequestBody): Observable<Void>
}