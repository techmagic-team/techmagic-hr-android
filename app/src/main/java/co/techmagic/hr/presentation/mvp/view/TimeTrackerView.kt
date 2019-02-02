package co.techmagic.hr.presentation.mvp.view

import co.techmagic.hr.presentation.pojo.UserReportViewModel

interface TimeTrackerView: View {
    fun showReports(reports : List<UserReportViewModel>)
}