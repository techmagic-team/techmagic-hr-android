package co.techmagic.hr.device.time_tracker.tracker_service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import co.techmagic.hr.R
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.presentation.ui.activity.HomeActivity
import co.techmagic.hr.presentation.util.TimeFormatUtil
import rx.Completable
import rx.Observable
import rx.Single
import rx.Subscription
import rx.observables.ConnectableObservable
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit


class HrAppTimeTrackerService : Service(), IHrAppTimeTracker {

    private var trackingReport: UserReport? = null
    private var timer: Observable<Long>? = null
    private var timerSubscription: Subscription? = null

    private var startTrackingMillis: Long = 0L

    private val timePassed: String
        get() {
            val elapsedTimeMillis = if (startTrackingMillis != 0L) System.currentTimeMillis() - startTrackingMillis else 0
            val timeMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis).toInt()
            return TimeFormatUtil.formatMinutesToHours(timeMinutes)
        }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel(TIME_TRACKER_CHANNEL_ID)

        when (intent?.action) {
            null -> showForeground(Action.ACTION_START)
            Action.ACTION_START.value -> showForeground(Action.ACTION_PAUSE, Action.ACTION_STOP)
            Action.ACTION_PAUSE.value -> showForeground(Action.ACTION_START)
            Action.ACTION_STOP.value -> showForeground(Action.ACTION_START)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("TEST_TIMER", "onBind")
        return HrAppTimeTrackerBinder(this)
    }

    override fun startTimer(userReport: UserReport): Completable {
        timer = Observable
                .timer(1, TimeUnit.SECONDS)
                .publish()

        (timer as ConnectableObservable).connect {
            timerSubscription = it
            NotificationManagerCompat.from(this).notify(FOREGROUND_NOTIFICATION_ID, createRunningTaskNotification())
        }

        startTrackingMillis = System.currentTimeMillis()

        return Completable.complete()
    }

    override fun stopTimer(reportId: String): Single<UserReport> {
        timerSubscription?.unsubscribe()
        return Single.just(trackingReport)
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        timer ?: throw NoTrackingReportException()
        return timer
                ?.flatMap { Observable.just(1, 2, 3, 4, 5, 6, 7, 8) }
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

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    channelId,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun showForeground(vararg actions: Action) {
        startForeground(FOREGROUND_NOTIFICATION_ID, createRunningTaskNotification())
    }

    private fun createRunningTaskNotification(): Notification {
        return createNotification(trackingReport, Action.ACTION_PAUSE, Action.ACTION_STOP)
    }

    private fun createNotification(userReport: UserReport?, vararg actions: Action): Notification {
        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, TIME_TRACKER_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .also {
                    if (userReport != null) {
                        it.setContentText(userReport?.task?.name + " " + timePassed) //TODO: concat task name and status
                    } else {
                        it.setContentText("")
                    }
                }
                .setSmallIcon(R.drawable.ic_techmagic_notification)
                .also {
                    if (actions.contains(Action.ACTION_START)) it.addAction(android.R.drawable.ic_media_play, "Start", startIntent())
                    if (actions.contains(Action.ACTION_PAUSE)) it.addAction(android.R.drawable.ic_media_pause, "Pause", stopIntent())
                    if (actions.contains(Action.ACTION_STOP)) it.addAction(R.drawable.ic_tracking_stop, "Stop", pauseIntent())
                }
                .setContentIntent(pendingIntent)
                .build()

        return notification
    }

    private fun startIntent() = PendingIntent.getService(this,
            0, Intent(this, HrAppTimeTrackerService::class.java).also { it.action = Action.ACTION_START.value }, 0)

    private fun stopIntent() = PendingIntent.getService(this,
            0, Intent(this, HrAppTimeTrackerService::class.java).also { it.action = Action.ACTION_PAUSE.value }, 0)

    private fun pauseIntent() = PendingIntent.getService(this,
            0, Intent(this, HrAppTimeTrackerService::class.java).also { it.action = Action.ACTION_STOP.value }, 0)

    companion object {
        const val TIME_TRACKER_CHANNEL_ID = "TIME_TRACKER_CHANNEL"
        const val FOREGROUND_NOTIFICATION_ID = 1
    }

    enum class Action(val value: String) {
        ACTION_START("ACTION_START"),
        ACTION_PAUSE("ACTION_PAUSE"),
        ACTION_STOP("ACTION_STOP")
    }
}