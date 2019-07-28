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
import rx.Completable
import rx.Observable
import rx.Single


@Suppress("UnstableApiUsage")
class TimeTrackerDataSource(private val applicationContext: Context) : ITimeTrackerDataSource {
    private var sConn: ServiceConnection? = null
    private var timeTracker: TimeTracker? = null

    override fun startTimer(userReport: UserReport): Completable {
        return Completable.create { subscriber ->
            val intent = Intent(applicationContext, HrAppTimeTrackerService::class.java)
            sConn = object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val tracker = service as TimeTracker
                    timeTracker = tracker
                    Log.d("TEST_TIMER", "onServiceConnected")
                    tracker.startTimer(userReport).doOnCompleted {
                        subscriber.onCompleted()
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

    override fun isRunning(reportId: String): Single<Boolean> =
            timeTracker?.isRunning(reportId) ?: throwNoService()

    override fun subscribeOnTimeUpdates(): Observable<UserReport> =
            timeTracker?.subscribeOnTimeUpdates() ?: throwNoService()

    override fun close() = timeTracker?.close() ?: throwNoService()

    private fun throwNoService(): Nothing = run { throw IllegalStateException("No currently connected services") }
}