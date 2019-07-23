package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.data.repository.time_tracker.ITimeTrackerDataSource
import rx.Completable
import rx.Observable
import rx.Single

class TimeTrackerDataSource : ITimeTrackerDataSource {

    override fun startTimer(userReport: UserReport): Completable {
        return Completable.complete()
    }

    override fun stopTimer(reportId: String): Single<UserReport> {
        return Single.error(Throwable("Not implemented"))
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        return Observable.error(Throwable("Not implemented"))
    }

    override fun getReport(reportId: String): Single<UserReport> {
        return Single.error(Throwable("Not implemented"))
    }

    override fun removeReport(reportId: String): Completable {
        return Completable.complete()
    }
}