package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.support.annotation.IntDef
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppReportPropertiesPresenter : BasePresenter<ReportPropertiesView, IReportPropertiesRouter>(), ReportPropertiesPresenter {

    @ReportProjectType
    var type = PROJECT

    var userId: String? = null

    var projectId: String? = null
    var firstDayOfWeek: Date? = null

    companion object {
        @IntDef()
        @Retention(AnnotationRetention.SOURCE)
        annotation class ReportProjectType

        const val PROJECT = 0
        const val TASK = 1
    }

    override fun onViewCreated(isInitial: Boolean) {
        super.onViewCreated(isInitial)
        when (type) {
            PROJECT -> loadProjects()
            TASK -> loadTasks()
        }
    }

    private fun loadProjects() {
        val projects = arrayListOf<ProjectViewModel>()
        projects.add(ProjectViewModel("Text", "Text some text"))
        projects.add(ProjectViewModel("Text", "Text some text"))
        projects.add(ProjectViewModel("Text", "Text some text"))
        projects.add(ProjectViewModel("Text", "Text some text"))
        projects.add(ProjectViewModel("Text", "Text some text"))
        projects.add(ProjectViewModel("Text2", "Text some text"))
        projects.add(ProjectViewModel("Text2", "Text some text"))
        projects.add(ProjectViewModel("Text2", "Text some text"))
        projects.add(ProjectViewModel("Text2", "Text some text"))


        view?.showProperties(projects)
    }

    private fun loadTasks() {

    }
}