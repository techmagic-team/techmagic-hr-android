package co.techmagic.hr.data.repository.time_tracker

import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Completable
import rx.Observable
import rx.Single

interface ITimeTrackerDataSource {
    fun startTimer(userReport: UserReport): Completable

    fun stopTimer(reportId : String): Single<UserReport>

    fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport>

    fun getReport(reportId : String): Single<UserReport>

    fun removeReport(reportId: String) : Completable
}