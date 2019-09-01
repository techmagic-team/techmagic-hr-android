package co.techmagic.hr.device.time_tracker.tracker_service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.data.repository.time_tracker.ITimeTrackerDataSource
import rx.Observable
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject


@Suppress("UnstableApiUsage")
class TimeTrackerDataSource(private val applicationContext: Context) : ITimeTrackerDataSource {
    private var sConn: ServiceConnection? = null
    private var timeTracker: TimeTracker? = null

    private val publish: PublishSubject<TaskUpdate> = PublishSubject.create()
    private var subscription: Subscription? = null

    override fun startTimer(userReport: UserReport, totalDayMinutes: Int): Single<TimerTasks> {
        return connect().flatMap { tracker ->
            subscription?.unsubscribe()
            subscription = tracker.subscribeOnTimeUpdates().subscribe {
                publish.onNext(it)
            }
            return@flatMap tracker.startTimer(userReport, totalDayMinutes)
        }
    }

    override fun pauseTimer(): Single<UserReport> = timeTracker?.pauseTimer() ?: throwNoService()

    override fun stopTimer(): Single<UserReport> = timeTracker?.stopTimer()?.doAfterTerminate {
        sConn?.let { applicationContext.unbindService(it) }
    } ?: throwNoService()

    override fun isRunning(): Single<RunningTask> =
            timeTracker?.isRunning() ?: Single.just(RunningTask(null))

    override fun subscribeOnTimeUpdates(): Observable<TaskUpdate> = publish.observeOn(AndroidSchedulers.mainThread())

    private fun connect(): Single<TimeTracker> {
        return Single.create {
            timeTracker?.let { tracker ->
                it.onSuccess(tracker)
                return@create
            }

            sConn = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val tracker = service as TimeTracker
                    timeTracker = tracker
                    it.onSuccess(tracker)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    timeTracker?.stopTimer()?.subscribe()
                    timeTracker = null
                    subscription?.unsubscribe()

                    if (!it.isUnsubscribed) {
                        it.onError(java.lang.IllegalStateException("Timer disconnected."))
                    }
                }
            }

            val intent = Intent(applicationContext, HrAppTimeTrackerService::class.java)
            ContextCompat.startForegroundService(applicationContext, intent)
            applicationContext.bindService(intent, sConn!!, Context.BIND_AUTO_CREATE)
        }
    }

    private fun throwNoService(): Nothing = run { throw IllegalStateException("No currently connected services") }
}