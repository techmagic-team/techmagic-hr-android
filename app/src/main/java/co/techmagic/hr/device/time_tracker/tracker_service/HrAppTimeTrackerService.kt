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
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

typealias Seconds = Long

class HrAppTimeTrackerService : Service(), IHrAppTimeTracker {

    private var trackingReportOrigin: UserReport? = null
    private var trackingReport: UserReport? = null

    private var timer: Observable<Seconds>? = null
    private var timerSubscription: Subscription? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel(TIME_TRACKER_CHANNEL_ID)

        when (intent?.action) {
            null -> showForeground()
            Action.ACTION_START.value -> trackingReport?.let { startTimer(it) }
            Action.ACTION_PAUSE.value ->  trackingReport?.let { stopTimer(it.id) } //todo: pause timer
            Action.ACTION_STOP.value -> trackingReport?.let { stopTimer(it.id) }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("TEST_TIMER", "onBind")
        return HrAppTimeTrackerBinder(this)
    }

    override fun startTimer(userReport: UserReport): Completable {
        return isRunning(userReport.id)
                .flatMap { isRunning ->
                    if (isRunning) {
                        return@flatMap Single.just(userReport)
                    } else {
                        return@flatMap stopTimer(userReport.id)
                    }
                }.flatMapCompletable {
                    Completable.create { subscriber ->
                        trackingReportOrigin = userReport.copy()
                        trackingReport = userReport.copy()

                        timer = Observable
                                .interval(1, 1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .publish().also {
                                    timerSubscription = it.connect()

                                    it.subscribe { secondsPassed ->
                                        trackingReport?.let { report ->
                                            val originalTime = trackingReportOrigin?.minutes ?: 0
                                            report.minutes = originalTime + TimeUnit.SECONDS.toMinutes(secondsPassed).toInt()
                                            updateTaskNotification()
                                        }
                                        subscriber.onCompleted()
                                    }
                                }
                    }
                }.also {
                    it.subscribe()
                }
    }

    override fun stopTimer(reportId: String): Single<UserReport> {
        // todo: no need for reportId as we already have instance of running report
        timerSubscription?.unsubscribe()
        updateTaskNotification()
        return Single.just(trackingReport)
    }

    override fun isRunning(reportId: String): Single<Boolean> {
        return Single.just(trackingReport?.id == reportId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        return timer?.let {
            return it.map { seconds ->
                //                userReport.minutes = (trackingReportOrigin?.minutes
//                        ?: 0) + TimeUnit.SECONDS.toMinutes(seconds).toInt()
                return@map userReport
            }
        } ?: Observable.just(userReport) //throw NoTrackingReportException() //todo: fix an issue
    }

    override fun getReport(reportId: String): Single<UserReport> {
        return isRunning(reportId).flatMap { running ->
            if (running)
                Single.just(trackingReport)
            else
                Single.error(IllegalStateException("Timer is not running for the report"))
        }
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

    private fun showForeground() {
        startForeground(FOREGROUND_NOTIFICATION_ID, createNotification("", emptyArray()))
    }

    private fun getActionsForCurrentState(): Array<Action> {
        return timerSubscription?.let {
            if (!it.isUnsubscribed) arrayOf(Action.ACTION_PAUSE, Action.ACTION_STOP)
            else arrayOf(Action.ACTION_START)
        } ?: emptyArray()
    }

    private fun updateTaskNotification() {
        trackingReport?.let { report ->
            NotificationManagerCompat.from(this).also {
                it.notify(FOREGROUND_NOTIFICATION_ID, createTaskNotification(report, getActionsForCurrentState()))
            }
        }
    }

    var ss = 0

    private fun createTaskNotification(report: UserReport, actions: Array<Action>): Notification {
        val text = String.format("%s %s",
                report.task.name,
                TimeFormatUtil.formatMinutesToHours(report.minutes)
                        + String.format(":%02d", ss++ % 60)) //todo: remove when is not needed anymore
        return createNotification(text, actions)
    }

    private fun createNotification(text: CharSequence, actions: Array<Action>): Notification {
        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, TIME_TRACKER_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
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