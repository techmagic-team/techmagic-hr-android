package co.techmagic.hr.data.store

import co.techmagic.hr.data.entity.Projects
import co.techmagic.hr.data.entity.Tasks
import co.techmagic.hr.data.entity.UserReports
import retrofit2.http.*
import rx.Observable

interface TimeTrackerApi {
    @GET("/v1/time-reporting/report")
    fun getReport(@Query("_user") userId: String, @Query("week") date: String): Observable<UserReports>

    @GET("/v1/time-reporting/project/projects/for-reporting")
    fun getProjects(@Query("_user") userId: String, @Query("date") week: String): Observable<Projects>

    @GET("/v1/time-reporting/project/{projectId}/task")
    fun getProjectTasks(@Query("projectId") projectId: String): Observable<Tasks>

    @POST("/v1/time-reporting/report/")
    fun createTask(): Observable<Void>

    @GET("/v1/time-reporting/report/{weekId}/day/{reportId}")
    fun getTask(@Query("weekId") weekId: String, @Query("reportId") reportId: String): Observable<Void>

    @PATCH("/v1/time-reporting/report/{weekId}/day/{reportId}")
    fun updateTask(@Query("weekId") weekId: String, @Query("reportId") reportId: String): Observable<Void>

    @DELETE("/v1/time-reporting/report/{weekId}/day/{reportId}")
    fun deleteTask(@Query("weekId") weekId: String, @Query("reportId") reportId: String): Observable<Void>

}