package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.support.annotation.IntDef
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ReportProperty
import com.techmagic.viper.base.BasePresenter
import java.util.*

class HrAppReportProjectPresenter : BasePresenter<ReportProjectsView, IReportProjectsRouter>(), ReportProjectsPresenter {

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
        val projects = arrayListOf<ReportProperty>()
        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 1"

            override fun getTitle() = "Title 1"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 1"

            override fun getTitle() = "Title 2"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 2"

            override fun getTitle() = "Title 1"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 2"

            override fun getTitle() = "Title 2"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 2"

            override fun getTitle() = "Title 3"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 2"

            override fun getTitle() = "Title 4"
        })

        projects.add(object : ReportProperty {
            override fun getParent() = "Parent 5"

            override fun getTitle() = "Title 1"
        })

        view?.showProperties(projects)
    }

    private fun loadTasks() {

    }
}