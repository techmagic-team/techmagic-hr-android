package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Completable
import rx.Observable
import rx.Single

interface IHrAppTimeTracker {
    fun startTimer(userReport: UserReport): Completable

    fun stopTimer(reportId : String): Single<UserReport>

    fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport>

    fun getReport(reportId : String): Single<UserReport>

    fun removeReport(reportId: String) : Completable

    fun close()
}