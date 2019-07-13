package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.data.repository.time_tracker.ITimeTrackerDataSource
import rx.Completable
import rx.Observable
import rx.Single

class TimeTrackerDataSource : ITimeTrackerDataSource {

    override fun startTimer(userReport: UserReport): Completable {

    }

    override fun stopTimer(reportId: String): Single<UserReport> {
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
    }

    override fun getReport(reportId: String): Single<UserReport> {
    }

    override fun removeReport(reportId: String): Completable {
    }
}