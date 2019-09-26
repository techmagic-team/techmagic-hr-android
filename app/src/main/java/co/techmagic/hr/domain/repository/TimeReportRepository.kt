package co.techmagic.hr.domain.repository

import co.techmagic.hr.data.entity.time_report.*
import rx.Completable
import rx.Observable
import rx.Single
import java.util.*

interface TimeReportRepository {
    fun getMe(): Observable<UserResponse>
    fun getDayReports(userId: String, firstDayOfWeek: Calendar): Observable<UserReportsResponse>
    fun getProjects(userId: String, firstDayOfWeek: String): Observable<List<ProjectResponse>>
    fun getProjectTasks(projectId: String): Observable<List<TaskResponse>>
    fun getTaskDetails(weekId: String, reportId: String): Observable<TaskDetailsResponse>
    fun reportTask(requestBody: ReportTaskRequestBody): Observable<ReportTaskResponse>
    fun updateTask(weekId: String, reportId: String, body: UpdateTaskRequestBody): Single<UpdateUserReportResponse>
    fun deleteTask(weekId: String, reportId: String, body: DeleteTaskRequestBody): Observable<Void>
    fun changeLastSelectedProject(projectResponse: ProjectResponse) : Completable
    fun changeLastSelectedTask(taskResponse : TaskResponse) : Completable
    fun getLastSelectedProject() : Observable<ProjectResponse>
    fun getLastSelectedTask() : Observable<TaskResponse>
}