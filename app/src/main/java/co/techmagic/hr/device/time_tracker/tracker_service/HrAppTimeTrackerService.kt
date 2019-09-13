package co.techmagic.hr.device.time_tracker.tracker_service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import co.techmagic.hr.R
import co.techmagic.hr.RepositoriesProvider
import co.techmagic.hr.data.entity.time_report.UpdateTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.HrAppBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.ui.activity.HomeActivity
import co.techmagic.hr.presentation.ui.activity.HomeActivity.EXTRA_OPEN_TIME_TRACKER
import co.techmagic.hr.presentation.util.*
import rx.Completable
import rx.Observable
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

typealias Seconds = Long

// rx.Completable is marker with @Beta annotation and considered unstable
@Suppress("UnstableApiUsage")
class HrAppTimeTrackerService : Service(), TimeTracker {

    private var trackingReportOrigin: UserReport? = null
    private var trackingReport: UserReport? = null

    private var timer: Observable<Seconds>? = null
    private var timerSubscription: Subscription? = null

    private lateinit var wakeLock: PowerManager.WakeLock

    private val publish: PublishSubject<TaskUpdate> = PublishSubject.create<TaskUpdate>().also {
        it.onBackpressureDrop()
    }

    private val userId
        get() = SharedPreferencesUtil.readUser().id // TODO: inject as a manager instance

    private val totalDayMinutes = 0

    private lateinit var reportRepository: TimeReportRepository

    override fun onCreate() {
        super.onCreate()
        reportRepository = (application as RepositoriesProvider).provideTimeReportRepository()
        turnOnWakeLock()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel(TIME_TRACKER_CHANNEL_ID)
        createNotificationChannel(TOO_MUCH_TIME_CHANNELE_ID)

        if (intent?.action == null) {
            showForeground()
        } else {
            trackingReport?.let { report ->
                when (intent.action) {
                    Action.ACTION_START.value -> startTimer(report, totalDayMinutes)
                            .subscribe(this::onCommandSuccess, this::onCommandError)
                    Action.ACTION_PAUSE.value -> pauseTimer()
                            .subscribe(this::onCommandSuccess, this::onCommandError)
                    Action.ACTION_STOP.value -> stopTimer()
                            .subscribe(this::onCommandSuccess, this::onCommandError)
                    else -> null
                }
            } ?: close()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return HrAppTimeTrackerBinder(this)
    }

    override fun startTimer(userReport: UserReport, totalDayMinutes: Int): Single<TimerTasks> {
        val report = userReport.copy()
        return isRunning()
                .flatMap { runningTask ->
                    if (runningTask.report == null || runningTask.report.id == report.id) {
                        return@flatMap Single.just(TimerTasks(null, report))
                    } else {
                        return@flatMap pauseTimer().map { TimerTasks(it, report) }
                    }
                }.doOnEach {
                    trackingReportOrigin = report.copy()
                    trackingReport = report.copy()

                    timer = Observable
                            .interval(1, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .publish().also {
                                timerSubscription = it.connect()

                                it.subscribe { secondsPassed ->
                                    trackingReport?.let { report ->
                                        val originalTime = trackingReportOrigin?.minutes ?: 0
                                        val newTime = originalTime + TimeUnit.SECONDS.toMinutes(secondsPassed).toInt()
                                        report.minutes = newTime
                                        if (newTime + totalDayMinutes < MAX_TRACKING_TIME_MINUTES) {
                                            updateTaskNotification(timerSubscription, secondsPassed)
                                            publish.onNext(TaskUpdate(report, TaskTimerState.RUNNING))
                                        } else {
                                            stopTimer().subscribe {
                                                showNotification(String.format("%s : %s", report.project, report.task.name),
                                                        getString(R.string.tm_hr_time_tracker_fragment_too_much_time_description),
                                                        TOO_MUCH_TIME_CHANNELE_ID
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                }
    }

    override fun pauseTimer(): Single<UserReport> {
        return Completable.create {
            timer = null
            timerSubscription?.unsubscribe()
            updateTaskNotification(timerSubscription, 0)
            it.onCompleted()
        }.andThen(updateReport())
    }

    override fun stopTimer(): Single<UserReport> {
        return pauseTimer().doAfterTerminate { close() }
    }

    override fun isRunning(): Single<RunningTask> {
        return Single.just(RunningTask(trackingReport?.copy()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun subscribeOnTimeUpdates(): Observable<TaskUpdate> = publish.observeOn(AndroidSchedulers.mainThread())

    private fun <T> onCommandSuccess(result: T) {
        /* no-op */
    }

    private fun onCommandError(error: Throwable) {
        error.printStackTrace()
        close()
    }

    private fun close() {
        timerSubscription = null
        updateTaskNotification(timerSubscription, 0)

        turnOffWakeLock()
        stopForeground(true)
        stopSelf()
        trackingReportOrigin = null
        trackingReport = null
    }

    private fun updateReport(): Single<UserReport> {
        return trackingReport?.let {
            val reportDate = it.date.toCalendar()
            return@let reportRepository.updateTask(it.weekReportId, it.id,
                    createUpdateTaskRequestBody(
                            reportDate.formatDate(ISO_WITH_TIME_ZONE_DATE_FORMAT),
                            reportDate.firstDayOfWeekDate().formatDate(), it.minutes, it.note,
                            null,
                            null,
                            userId
                    ))
                    .map { response ->
                        response.report?.also { updatedReport ->
                            trackingReportOrigin = updatedReport.copy()
                            trackingReport = updatedReport.copy()
                            publish.onNext(TaskUpdate(updatedReport, TaskTimerState.STOPPED))
                        } ?: it
                    }
        } ?: Single.error(java.lang.IllegalStateException())
    }

    private fun createUpdateTaskRequestBody(date: String,
                                            firstDayOfWeek: String,
                                            hours: Int,
                                            note: String,
                                            projectId: String?,
                                            taskId: String?,
                                            userId: String) = UpdateTaskRequestBody(date, firstDayOfWeek, hours, note, HrAppBaseTimeReportDetailPresenter.RATE, projectId, taskId, userId)

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    channelId,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            )
            serviceChannel.setSound(null, null)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun showForeground() {
        startForeground(FOREGROUND_NOTIFICATION_ID, createNotification())
    }

    private fun getActionsForCurrentState(subscription: Subscription): Array<Action> {
        return if (!subscription.isUnsubscribed) arrayOf(Action.ACTION_PAUSE, Action.ACTION_STOP)
        else arrayOf(Action.ACTION_START)
    }

    private fun updateTaskNotification(subscription: Subscription?, seconds: Long) {
        val notificationManager = NotificationManagerCompat.from(this)
        trackingReport?.let { report ->
            subscription?.let {
                val actions = getActionsForCurrentState(it)
                notificationManager.notify(FOREGROUND_NOTIFICATION_ID, createTaskNotification(report, seconds, actions))
            }
        } ?: notificationManager.cancel(FOREGROUND_NOTIFICATION_ID)
    }

    private fun showNotification(title: CharSequence, message: CharSequence, channel: String) {
        NotificationManagerCompat.from(this).also {
            val notificationManaпer = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = createDefaultNotificationBuilder(
                    title,
                    message,
                    channel,
                    getDefaultNotificationPendingIntent()
            ).build()
            notification.flags = Notification.FLAG_AUTO_CANCEL
            notificationManaпer.notify(TOO_MUCH_TIME_NOTIFICATION_ID, notification)
        }
    }

    private fun createTaskNotification(report: UserReport, seconds: Long, actions: Array<Action>): Notification {
        val title = report.project
        val text = String.format("%s %s:%02d   \n%s",
                report.task.name,
                TimeFormatUtil.formatMinutesToHours(report.minutes), seconds % 60,
                report.note)
        return createNotification(title, text, text, actions)
    }

    private fun createNotification(title: CharSequence = getString(R.string.app_name),
                                   text: CharSequence = "",
                                   bigText: CharSequence = "",
                                   actions: Array<Action> = emptyArray()): Notification {


        return createDefaultNotificationBuilder(title, text, TIME_TRACKER_CHANNEL_ID, getDefaultNotificationPendingIntent())
                .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
                .also {
                    if (actions.contains(Action.ACTION_START)) it.addAction(R.drawable.ic_notification_play_24px, "Start", startIntent())
                    if (actions.contains(Action.ACTION_PAUSE)) it.addAction(R.drawable.ic_notification_pause_24px, "Pause", pauseIntent())
                    if (actions.contains(Action.ACTION_STOP)) it.addAction(R.drawable.ic_notification_stop_24px, "Stop", stopIntent())
                }
                .build()
    }

    private fun createDefaultNotificationBuilder(title: CharSequence,
                                                 message: CharSequence,
                                                 channelId: String,
                                                 pendingIntent: PendingIntent): NotificationCompat.Builder {

        return NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_techmagic_notification)
                .setContentIntent(pendingIntent)
    }

    private fun getDefaultNotificationPendingIntent(): PendingIntent {
        val notificationIntent = Intent(this, HomeActivity::class.java)
        notificationIntent.putExtra(EXTRA_OPEN_TIME_TRACKER, true)
        notificationIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun startIntent() = PendingIntent.getService(this,
            0,
            Intent(this, HrAppTimeTrackerService::class.java)
                    .also { it.action = Action.ACTION_START.value },
            0)

    private fun stopIntent() = PendingIntent.getService(this,
            0,
            Intent(this, HrAppTimeTrackerService::class.java)
                    .also { it.action = Action.ACTION_STOP.value },
            0)

    private fun pauseIntent() = PendingIntent.getService(this,
            0,
            Intent(this, HrAppTimeTrackerService::class.java)
                    .also { it.action = Action.ACTION_PAUSE.value },
            0)

    @SuppressLint("WakelockTimeout")
    private fun turnOnWakeLock() {
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG)
        wakeLock.acquire()
    }

    private fun turnOffWakeLock() {
        wakeLock.release()
    }

    companion object {
        const val TIME_TRACKER_CHANNEL_ID = "TIME_TRACKER_CHANNEL"
        const val FOREGROUND_NOTIFICATION_ID = 1

        const val TOO_MUCH_TIME_NOTIFICATION_ID = 2
        const val TOO_MUCH_TIME_CHANNELE_ID = "TOO_MUCH_TIME_CHANNELE"

        const val WAKE_LOCK_TAG = "WakeLock::lock"
    }

    enum class Action(val value: String) {
        ACTION_START("ACTION_START"),
        ACTION_PAUSE("ACTION_PAUSE"),
        ACTION_STOP("ACTION_STOP")
    }
}