package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.base.HrAppBaseViewFragment
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter.Companion.PROJECT
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter.Companion.TASK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ReportProjectsAdapter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.ReportPropertyHeaderItemDecorator
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter.TasksAdapter
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.BaseHeadersAdapter
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import java.util.*

class ReportPropertiesFragment : HrAppBaseViewFragment<ReportPropertiesPresenter>(), ReportPropertiesView {

    private lateinit var rvReportProperties: RecyclerView
    private lateinit var tvEmptyError: TextView

    private var projectsAdapter: ReportProjectsAdapter? = null
    private var tasksAdapter: TasksAdapter? = null
    private lateinit var toolbarChangeListener: ActionBarChangeListener

    companion object {
        const val ARG_TYPE = "arg_type"
        const val ARG_USER_ID = "arg_user_id"
        const val ARG_FIRST_DAY_OF_WEEK = "arg_first_day_of_week"
        const val ARG_PROJECT_ID = "arg_project_id"

        fun newProjectsInstance(userId: String, firstDayOfWeek: Calendar): ReportPropertiesFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, PROJECT)
            args.putString(ARG_USER_ID, userId)
            args.putSerializable(ARG_FIRST_DAY_OF_WEEK, firstDayOfWeek)

            val fragment = ReportPropertiesFragment()
            fragment.arguments = args

            return fragment
        }

        fun newTasksInstance(projectId: String): ReportPropertiesFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, TASK)
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

    override fun showTitle(titleRes: Int) {
        toolbarChangeListener.setActionBarTitle(getString(titleRes))
    }

    override fun showProperties(props: List<ProjectViewModel>) {
        if (projectsAdapter == null) {
            projectsAdapter = ReportProjectsAdapter()
            projectsAdapter!!.clickListener = object : BaseHeadersAdapter
            .HeaderAdapterItemClickListener<ProjectViewModel> {
                override fun onItemClick(item: ProjectViewModel) {
                    presenter?.onProjectClicked(item)
                }
            }
            rvReportProperties.addItemDecoration(ReportPropertyHeaderItemDecorator<ProjectViewModel>())
            rvReportProperties.adapter = projectsAdapter
        }
        projectsAdapter?.setData(props)
    }

    override fun showTasks(props: List<ProjectTaskViewModel>) {
        if (tasksAdapter == null) {
            tasksAdapter = TasksAdapter()
            tasksAdapter!!.clickListener = object : BaseHeadersAdapter
            .HeaderAdapterItemClickListener<ProjectTaskViewModel> {
                override fun onItemClick(item: ProjectTaskViewModel) {
                    presenter?.onProjectTaskClicked(item)
                }
            }
            rvReportProperties.addItemDecoration(ReportPropertyHeaderItemDecorator<ProjectTaskViewModel>())
            rvReportProperties.adapter = tasksAdapter
        }
        tasksAdapter?.setData(props)
    }

    override fun showEmptyList(@StringRes type: Int) {
        val typeString = getString(type)
        tvEmptyError.visibility = View.VISIBLE
        tvEmptyError.text = getString(R.string.tm_hr_report_you_have_no_data, typeString)
    }

    private fun findViews(view: View) {
        rvReportProperties = view.findViewById(R.id.rvReportProperties)
        tvEmptyError = view.findViewById(R.id.tvEmptyError)

        initRecycler()
    }

    private fun initRecycler() {
        rvReportProperties.adapter = projectsAdapter
    }
}