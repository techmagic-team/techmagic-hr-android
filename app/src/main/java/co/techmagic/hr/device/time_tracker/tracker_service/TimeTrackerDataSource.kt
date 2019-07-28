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


class TimeTrackerDataSource(val applicationContext: Context) : ITimeTrackerDataSource {
    var sConn: ServiceConnection? = null
    var intent: Intent? = null
    var timeTracker: IHrAppTimeTracker? = null

    override fun startTimer(userReport: UserReport): Completable {
        return Completable.create { subscriber ->
            val intent = Intent(applicationContext, HrAppTimeTrackerService::class.java)
            sConn = object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    timeTracker = (service as HrAppTimeTrackerBinder).timeTracker
                    Log.d("TEST_TIMER", "onServiceConnected")
                    timeTracker?.startTimer(userReport)?.let {
                        subscriber.onCompleted()
                    } ?: subscriber.onCompleted()
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

    override fun stopTimer(reportId: String): Single<UserReport> {
        timeTracker ?: throw IllegalStateException("No currently connected services")
        applicationContext.unbindService(sConn!!)
        return timeTracker!!.stopTimer()
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        timeTracker ?: throw IllegalStateException("No currently connected services")
        return timeTracker!!.subscribeOnTimeUpdates(userReport)
    }

    override fun getReport(reportId: String): Single<UserReport> {
        timeTracker ?: throw IllegalStateException("No currently connected services")
        return timeTracker!!.getReport(reportId)
    }

    override fun removeReport(reportId: String): Completable {
        timeTracker ?: throw IllegalStateException("No currently connected services")
        return timeTracker!!.removeReport(reportId)
    }
}