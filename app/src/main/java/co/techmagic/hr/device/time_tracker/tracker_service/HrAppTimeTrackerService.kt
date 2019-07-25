package co.techmagic.hr.device.time_tracker.tracker_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Completable
import rx.Observable
import rx.Single
import rx.Subscription
import rx.observables.ConnectableObservable
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class HrAppTimeTrackerService : Service(), IHrAppTimeTracker {

    private var trackingReport: UserReport? = null
    private var timer: Observable<Long>? = null
    private var timerSubscription: Subscription? = null

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("TEST_TIMER", "onBind")
        return HrAppTimeTrackerBinder(this)
    }

    override fun startTimer(userReport: UserReport): Completable {
        timer = Observable
                .timer(1, TimeUnit.SECONDS)
                .publish()

        timerSubscription = (timer as ConnectableObservable).connect()

        return Completable.complete()
    }

    override fun stopTimer(reportId: String): Single<UserReport> {
        timerSubscription?.unsubscribe()
        return Single.just(trackingReport)
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        timer ?: throw NoTrackingReportException()
        return timer
                ?.flatMap { Observable.just(1,2, 3, 4, 5, 6, 7, 8) }
                ?.map {
                    userReport.minutes = (it / 60).toInt()
                    return@map userReport
                }!!
    }

    override fun getReport(reportId: String): Single<UserReport> {
        return Single.just(trackingReport)
    }

    override fun removeReport(reportId: String): Completable {
        throw IllegalArgumentException("Do not implement this method")
        return Completable.complete()
    }

    override fun close() {
        //TODO impl
    }
}