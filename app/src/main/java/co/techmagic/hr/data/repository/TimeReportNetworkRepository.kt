package co.techmagic.hr.data.repository

import co.techmagic.hr.data.entity.Projects
import co.techmagic.hr.data.entity.UserReports
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

    override fun getReport(userId: String, date: String): Observable<UserReports> {
        return setup(apiClient.getReport(userId, date))
    }

    override fun getProjects(userId: String, firstDayOfWeek: String): Observable<Projects> {
        return setup(apiClient.getProjects(userId, firstDayOfWeek))
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