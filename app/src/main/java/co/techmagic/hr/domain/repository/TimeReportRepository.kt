package co.techmagic.hr.domain.repository

import co.techmagic.hr.data.entity.Projects
import co.techmagic.hr.data.entity.UserReports
import rx.Observable

interface TimeReportRepository {
    fun getReport(userId: String, date: String): Observable<UserReports>
    fun getProjects(userId: String, firstDayOfWeek: String): Observable<Projects>
}