package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.TaskViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter.Companion.PROJECT
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter.Companion.TASK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ProjectsAdapter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ReportPropertyHeaderItemDecorator
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.TasksAdapter
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import com.techmagic.viper.base.BaseViewFragment
import java.util.*

class ReportPropertiesFragment : BaseViewFragment<ReportProjectsPresenter>(), ReportProjectsView {

    private lateinit var rvReportProperties: RecyclerView

    private var projectAdapter: ProjectsAdapter? = null
    private var tasksAdapter: TasksAdapter? = null
    private lateinit var toolbarChangeListener: ActionBarChangeListener

    companion object {
        const val ARG_TYPE = "arg_type"
        const val ARG_USER_ID = "arg_user_id"
        const val ARG_FIRST_DAY_OF_WEEK = "arg_first_day_of_week"
        const val ARG_PROJECT_ID = "arg_project_id"

        fun newProjectsInstance(userId: String, firstDayOfWeek: Date): ReportPropertiesFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, TASK)
            args.putString(ARG_USER_ID, userId)
            args.putSerializable(ARG_FIRST_DAY_OF_WEEK, firstDayOfWeek)

            val fragment = ReportPropertiesFragment()
            fragment.arguments = args

            return fragment
        }

        fun newTasksInstance(projectId: String): ReportPropertiesFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, PROJECT)
            args.putString(ARG_PROJECT_ID, projectId)


            val fragment = ReportPropertiesFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_report_projects, container, false)

        findViews(view)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        toolbarChangeListener = context as ActionBarChangeListener
    }

    override fun showProperties(props: List<ProjectViewModel>) {
        toolbarChangeListener.setActionBarTitle(getString(R.string.tm_hr_report_select_project))
        if (projectAdapter == null) {
            projectAdapter = ProjectsAdapter()
            rvReportProperties.addItemDecoration(ReportPropertyHeaderItemDecorator<ProjectViewModel>())
            rvReportProperties.adapter = projectAdapter
        }
        projectAdapter?.setData(props)
    }

    override fun showTasks(props: List<TaskViewModel>) {
        toolbarChangeListener.setActionBarTitle(getString(R.string.tm_hr_report_select_task))
        if (tasksAdapter == null) {
            tasksAdapter = TasksAdapter()
            rvReportProperties.addItemDecoration(ReportPropertyHeaderItemDecorator<TaskViewModel>())
            rvReportProperties.adapter = tasksAdapter
        }
        tasksAdapter?.setData(props)
    }

    private fun findViews(view: View) {
        rvReportProperties = view.findViewById(R.id.rvReportProperties)

        initRecycler()
    }

    private fun initRecycler() {
        rvReportProperties.adapter = projectAdapter
    }
}