package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ReportProperty
import com.techmagic.viper.Presenter
import com.techmagic.viper.View

interface ReportProjectsView : View {
    fun showProperties(props: List<ReportProperty>)
}

interface ReportProjectsPresenter : Presenter {

}