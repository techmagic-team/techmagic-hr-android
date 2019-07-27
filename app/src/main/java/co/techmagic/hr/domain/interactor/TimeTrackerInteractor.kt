package co.techmagic.hr.domain.interactor

import co.techmagic.hr.data.entity.time_report.UpdateUserReportResponse
import co.techmagic.hr.data.entity.time_report.UserReport
import co.techmagic.hr.domain.repository.ITimeTrackerRepository
import rx.Single

class TimeTrackerInteractor(val timeTrackerRepository: ITimeTrackerRepository) {

//    fun isTracking(reportId: String) = timeTrackerRepository.isTracking(reportId)

    fun startTimer(timeReport: UserReport) = timeTrackerRepository.startTimer(timeReport)

    //todo: remove params
    fun stopTimer(userReportId: String, date: String, firstDayOfWeek: String): Single<UpdateUserReportResponse> {
        return timeTrackerRepository.stopTimer(userReportId, date, firstDayOfWeek)
    }

    //todo: remove params
    fun subscribeOnTimerUpdates(userReport: UserReport) = timeTrackerRepository.subscribeOnTimeUpdates(userReport)
}