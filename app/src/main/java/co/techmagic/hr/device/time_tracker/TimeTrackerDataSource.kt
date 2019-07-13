package co.techmagic.hr.device.time_tracker

import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.data.repository.time_tracker.ITimeTrackerDataSource
import rx.Completable
import rx.Observable
import rx.Single
import java.util.concurrent.TimeUnit

class TimeTrackerDataSource : ITimeTrackerDataSource {

    private val userReports = arrayListOf<UserReport>()

    override fun startTimer(userReport: UserReport): Completable {
        userReports.add(userReport)
        return Completable.complete()
    }

    override fun stopTimer(reportId: String): Single<UserReport> {
        return getReport(reportId)
                .doOnSuccess{removeReport(reportId).await()}//todo check await
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        return Observable
                .from(userReports)
                .debounce(2000, TimeUnit.MILLISECONDS)
    }

    override fun getReport(reportId: String): Single<UserReport> {
        val reportForStop = userReports.find { it.id == reportId }
        return when (reportForStop) {
            null -> throw NoReportException(reportId)
            else -> Single.just(reportForStop)
        }
    }

    override fun removeReport(reportId: String): Completable {
        return getReport(reportId)
                .flatMapCompletable { removeReport(it) }
    }

    private fun removeReport(userReport: UserReport): Completable {
        return Completable.fromAction { userReports.remove(userReport) }
    }

}