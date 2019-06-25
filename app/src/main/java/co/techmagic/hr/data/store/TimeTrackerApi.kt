package co.techmagic.hr.data.store

import co.techmagic.hr.data.entity.time_tracker.*
import retrofit2.http.*
import rx.Observable

interface TimeTrackerApi {
    @GET("/v1/users/me")
    fun getMe(): Observable<UserResponse>

    @GET("/v1/time-reporting/report")
    fun getReports(@Query("_user") userId: String,
                   @Query("week") date: String): Observable<UserReportsResponse>

    @GET("/v1/time-reporting/project/projects/for-reporting")
    fun getProjects(@Query("_user") userId: String,
                    @Query("date") week: String): Observable<List<ProjectResponse>>

    @GET("/v1/time-reporting/project/{projectId}/task")
    fun getProjectTasks(@Path("projectId") projectId: String): Observable<List<TaskResponse>>

    @GET("/v1/time-reporting/report/{weekId}/day/{reportId}")
    fun getTaskDetails(@Path("weekId") weekId: String,
                       @Path("reportId") reportId: String): Observable<TaskDetailsResponse>

    @POST("/v1/time-reporting/report/")
    fun reportTask(@Body requestBody: ReportTaskRequestBody): Observable<ReportTaskResponse>

    @PATCH("/v1/time-reporting/report/{weekId}/day/{reportId}")
    fun updateTask(@Path("weekId") weekId: String,
                   @Path("reportId") reportId: String,
                   @Body body: UpdateTaskRequestBody): Observable<UpdateUserReportResponse>

    @HTTP(method = "DELETE", path = "/v1/time-reporting/report/{weekId}/day/{reportId}", hasBody = true)
    // @DELETE("/v1/time-reporting/report/{weekId}/day/{reportId}") -> IllegalArgumentException: Non-body HTTP method cannot contain @Body.
    fun deleteTask(@Path("weekId") weekId: String,
                   @Path("reportId") reportId: String,
                   @Body body: DeleteTaskRequestBody): Observable<Void>
}