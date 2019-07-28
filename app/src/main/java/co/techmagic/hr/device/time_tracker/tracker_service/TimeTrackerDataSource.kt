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
import rx.subjects.BehaviorSubject


@Suppress("UnstableApiUsage")
class TimeTrackerDataSource(private val applicationContext: Context) : ITimeTrackerDataSource {
    private var sConn: ServiceConnection? = null
    private var timeTracker: TimeTracker? = null

    private val publish: BehaviorSubject<TaskUpdate> = BehaviorSubject.create()

    override fun startTimer(userReport: UserReport): Single<TimerTasks> {
        return Single.create {
            val intent = Intent(applicationContext, HrAppTimeTrackerService::class.java)
            sConn = object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val tracker = service as TimeTracker
                    timeTracker = tracker
                    Log.d("TEST_TIMER", "onServiceConnected")
                    tracker.startTimer(userReport).subscribe { tasks ->
                        it.onSuccess(tasks)
                    }

                    tracker.subscribeOnTimeUpdates().subscribe {
                        publish.onNext(it)
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    Log.d("TEST_TIMER", "onServiceDisconnected")
                    timeTracker?.close()
                }
            }

            ContextCompat.startForegroundService(applicationContext, intent)
            applicationContext.bindService(intent, sConn!!, Context.BIND_AUTO_CREATE)
        }
    }

    override fun pauseTimer(): Single<UserReport> = timeTracker?.pauseTimer() ?: throwNoService()

    override fun stopTimer(): Single<UserReport> = timeTracker?.stopTimer() ?: throwNoService()

    override fun isRunning(): Single<RunningTask> =
            timeTracker?.isRunning() ?: Single.just(RunningTask(null))

    override fun subscribeOnTimeUpdates(): Observable<TaskUpdate> = publish

    override fun close() = timeTracker?.close() ?: throwNoService()

    private fun throwNoService(): Nothing = run { throw IllegalStateException("No currently connected services") }
}