package co.techmagic.hr.presentation.mvp.view

import co.techmagic.hr.presentation.pojo.TimeReportViewModel

interface TimeTrackerView: View {
    fun showReports(reports : List<TimeReportViewModel>)
}