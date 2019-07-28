package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Completable
import rx.Observable
import rx.Single

interface TimeTracker {
    fun startTimer(userReport: UserReport): Completable

    fun pauseTimer(): Single<UserReport>

    fun stopTimer(): Single<UserReport>

    fun isRunning(reportId : String): Single<Boolean>

    fun subscribeOnTimeUpdates(): Observable<UserReport>

    fun close()
}