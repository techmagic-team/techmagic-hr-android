package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.presentation.util.TimeFormatUtil
import rx.Observable
import rx.Single

data class TimerTasks(val previous: UserReport?, val current: UserReport)

data class RunningTask(val report: UserReport?)

data class TaskUpdate(val report: UserReport, val state: TaskTimerState)

enum class TaskTimerState {
    RUNNING,
    STOPPED
}

const val MAX_TRACKING_TIME_MINUTES = 16 * TimeFormatUtil.MINUTES_IN_ONE_HOUR

interface TimeTracker {
    fun startTimer(userReport: UserReport, totalDayMinutes: Int): Single<TimerTasks>

    fun pauseTimer(): Single<UserReport>

    fun stopTimer(): Single<UserReport>

    fun isRunning(): Single<RunningTask>

    fun subscribeOnTimeUpdates(): Observable<TaskUpdate>

    fun close()
}