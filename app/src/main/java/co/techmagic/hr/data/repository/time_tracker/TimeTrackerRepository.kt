package co.techmagic.hr.data.repository.time_tracker

import co.techmagic.hr.data.entity.time_report.UpdateTaskRequestBody
import co.techmagic.hr.data.entity.time_report.UpdateUserReportResponse
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.domain.repository.ITimeTrackerRepository
import co.techmagic.hr.domain.repository.TimeReportRepository
import rx.Completable
import rx.Observable
import rx.Single

class TimeTrackerRepository(private val timeTrackerDataSource: ITimeTrackerDataSource,
                            private val timeReportRepository: TimeReportRepository)
    : ITimeTrackerRepository {

    override fun startTimer(userReport: UserReport): Completable {
        return timeTrackerDataSource.startTimer(userReport)
    }

    override fun stopTimer(reportId: String, date: String, firstDayOfWeek: String): Single<UpdateUserReportResponse> {
        return timeTrackerDataSource
                .getReport(reportId)
                .flatMap {
                    timeReportRepository
                            .updateTask(it.weekReportId, it.id, createUpdateTimeRequestBody(it, date, firstDayOfWeek))
                }
                .doOnSuccess {
                    timeTrackerDataSource.removeReport(it.report!!.id).await()//todo check await
                }
    }

    override fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport> {
        return timeTrackerDataSource
                .subscribeOnTimeUpdates(userReport)
    }

    private fun createUpdateTimeRequestBody(userReport: UserReport, date: String, firstDayOfWeek: String): UpdateTaskRequestBody {
        return UpdateTaskRequestBody(userReport.id, date, firstDayOfWeek, userReport.minutes)
    }
}