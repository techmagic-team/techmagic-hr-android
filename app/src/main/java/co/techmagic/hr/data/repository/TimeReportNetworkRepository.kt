package co.techmagic.hr.data.repository

import co.techmagic.hr.data.entity.time_tracker.*
import co.techmagic.hr.data.exception.NetworkConnectionException
import co.techmagic.hr.data.manager.NetworkManager
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.domain.repository.TimeReportRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TimeReportNetworkRepository(
        private val apiClient: TimeTrackerApi,
        private val networkManager: NetworkManager) : TimeReportRepository {

    override fun getMe(): Observable<UserResponse> {
        return setup(apiClient.getMe())
    }

    override fun getDayReports(userId: String, date: String): Observable<UserReportsResponse> {
        return setup(apiClient.getReports(userId, date))
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

    override fun updateTask(weekId: String, reportId: String, body: UpdateTaskRequestBody): Observable<UserReportsResponse> {
        return setup(apiClient.updateTask(weekId, reportId, body))
    }

    override fun deleteTask(weekId: String, reportId: String, body: DeleteTaskRequestBody): Observable<Void> {
        return setup(apiClient.deleteTask(weekId, reportId, body))
    }

    private fun <T> setup(observable: Observable<T>): Observable<T> {
        return if (networkManager.isNetworkAvailable) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            Observable.error(NetworkConnectionException())
        }
    }
}