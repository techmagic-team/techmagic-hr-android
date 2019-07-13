package co.techmagic.hr.domain.repository

import co.techmagic.hr.data.entity.time_report.UpdateUserReportResponse
import co.techmagic.hr.data.entity.time_report.UserReport
import rx.Completable
import rx.Observable
import rx.Single

interface ITimeTrackerRepository {

    /**
     * Starts timer.
     * @param userReport - report for tracking. Should be already created
     */
    fun startTimer(userReport: UserReport): Completable

    /**
     * Stops timer.
     * @param reportId - reports id for stopping tracking
     * @return updated user report
     */
    fun stopTimer(reportId: String, date: String, firstDayOfWeek: String): Single<UpdateUserReportResponse>

    /**
     * @param userReport - report, for handling time changes
     * @return Observable, subscribed on updating
     */
    fun subscribeOnTimeUpdates(userReport: UserReport): Observable<UserReport>

    /*
     TODO need features for getting currently tracking reports and report status updates
     fun subscribeOnTrackerReportsUpdates() : Observable<UserReport>*/
}