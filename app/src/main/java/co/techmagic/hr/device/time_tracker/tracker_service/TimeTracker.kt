package co.techmagic.hr.device.time_tracker.tracker_service

import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Observable
import rx.Single

data class TimerTasks(val previous: UserReport?, val current: UserReport)

data class RunningTask(val report: UserReport?)

data class TaskUpdate(val report: UserReport, val state: TaskTimerState)

enum class TaskTimerState {
    RUNNING,
    STOPPED
}

interface TimeTracker {
    fun startTimer(userReport: UserReport): Single<TimerTasks>

    fun pauseTimer(): Single<UserReport>

    fun stopTimer(): Single<UserReport>

    fun isRunning(): Single<RunningTask>

    fun subscribeOnTimeUpdates(): Observable<TaskUpdate>

    fun close()
}