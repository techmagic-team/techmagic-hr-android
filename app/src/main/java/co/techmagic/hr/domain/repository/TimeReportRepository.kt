package co.techmagic.hr.domain.repository

import co.techmagic.hr.data.entity.time_tracker.*
import rx.Observable

interface TimeReportRepository {
    fun getDayReports(userId: String, date: String): Observable<UserReportsResponse>
    fun getProjects(userId: String, firstDayOfWeek: String): Observable<List<ProjectResponse>>
    fun getProjectTasks(projectId: String): Observable<List<TaskResponse>>
    fun getTaskDetails(weekId: String, reportId: String): Observable<TaskDetailsResponse>
    fun reportTask(requestBody: ReportTaskRequestBody): Observable<ReportTaskResponse>
    fun updateTask(weekId: String, reportId: String, body: UpdateTaskRequestBody): Observable<UserReportsResponse>
    fun deleteTask(weekId: String, reportId: String, body: DeleteTaskRequestBody): Observable<Void>
}