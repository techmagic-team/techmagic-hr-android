package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.techmagic.hr.R
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.repository.TimeReportNetworkRepository
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report.HrAppCreateTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter.Companion.PROJECT
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter.Companion.ReportProjectType
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportPropertiesPresenter.Companion.TASK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.IReportPropertiesRouter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment.Companion.ARG_FIRST_DAY_OF_WEEK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment.Companion.ARG_PROJECT_ID
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment.Companion.ARG_TYPE
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportPropertiesFragment.Companion.ARG_USER_ID
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectTaskViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.ProjectViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report.HrAppUpdateTimReportDetailPresenter
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import com.techmagic.viper.base.BasePresenter
import java.util.*


class TimeReportDetailActivity : AppCompatActivity(), ActionBarChangeListener {

    companion object {
        const val EXTRA_USER_REPORT = "extra_time_report"
        const val EXTRA_REPORT_DATE = "extra_report_date"
        const val EXTRA_OLD_ID = "EXTRA_OLD_ID"

        fun start(fragment: Fragment, userReportForEdit: UserReportViewModel?, reportDate: Calendar, requestCode : Int) {
            val intent = Intent(fragment.activity, TimeReportDetailActivity::class.java)

            intent.putExtra(EXTRA_USER_REPORT, userReportForEdit)
            intent.putExtra(EXTRA_REPORT_DATE, reportDate)

            fragment.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var timeReportDetailPresenter: HrAppBaseTimeReportDetailPresenter
    private lateinit var reportPropertiesPresenter: HrAppReportPropertiesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_report_detail)
        init()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceTimeReportDetailFragment()

    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is TimeReportDetailFragment -> {
                val okHttpClientClient = ApiClient.buildOkHttpClientClient()
                val retrofit = ApiClient.getRetrofit(okHttpClientClient)
                val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)

                val timeReportRepository = TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager())
                val timeReportRouter = TimeReportDetailRouter(this, fragment)

                val timeReportDate = getReportDateFromExtra()//todo refactor changes
                timeReportDate.firstDayOfWeek = Calendar.MONDAY

                if (getUserReportForEdit() == null) {
                    timeReportDetailPresenter = HrAppCreateTimeReportDetailPresenter(timeReportRepository, UserReportViewModelMapper())
                } else {
                    //todo crate another presenter
                    timeReportDetailPresenter = HrAppUpdateTimReportDetailPresenter(timeReportRepository, UserReportViewModelMapper(), ProjectViewModelMapper(), ProjectTaskViewModelMapper())
                    (timeReportDetailPresenter as HrAppUpdateTimReportDetailPresenter).userReportForEdit = getUserReportForEdit()
                }

                timeReportDetailPresenter.reportDate = timeReportDate
                supportFragmentManager.addOnBackStackChangedListener {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    currentFragment?.userVisibleHint = true
                }

                BasePresenter.bind(fragment, timeReportDetailPresenter, timeReportRouter)
            }
            is ReportPropertiesFragment -> {
                @ReportProjectType val type = fragment.arguments?.getInt(ARG_TYPE)
                val projectId = fragment.arguments?.getString(ARG_PROJECT_ID)
                val userId = fragment.arguments?.getString(ARG_USER_ID)
                val firstDayOfWeek = fragment.arguments?.getSerializable(ARG_FIRST_DAY_OF_WEEK) as? Calendar

                //todo remove duplicated di code
                val okHttpClientClient = ApiClient.buildOkHttpClientClient()
                val retrofit = ApiClient.getRetrofit(okHttpClientClient)
                val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)
                val timeReportRepository = TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager())

                reportPropertiesPresenter = HrAppReportPropertiesPresenter(
                        timeReportRepository,
                        ProjectViewModelMapper(),
                        ProjectTaskViewModelMapper())

                val reportPropertiesRouter = object : IReportPropertiesRouter {
                    override fun closeWithProject(projectViewModel: ProjectViewModel) {
                        timeReportDetailPresenter.projectViewModel = projectViewModel
                        supportFragmentManager.popBackStack()
                    }

                    override fun closeWithProjectTask(projectTaskViewModel: ProjectTaskViewModel) {
                        timeReportDetailPresenter.projectTaskViewModel = projectTaskViewModel
                        supportFragmentManager.popBackStack()
                    }
                }

                reportPropertiesPresenter.type = type!!

                when (type) {
                    PROJECT -> {
                        reportPropertiesPresenter.userId = userId
                        reportPropertiesPresenter.firstDayOfWeek = firstDayOfWeek
                    }
                    TASK -> {
                        reportPropertiesPresenter.projectId = projectId
                    }
                }

                BasePresenter.bind(fragment, reportPropertiesPresenter, reportPropertiesRouter)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceTimeReportDetailFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, TimeReportDetailFragment.newInstance())
                .commit()
    }

    private fun getUserReportForEdit(): UserReportViewModel? = intent.getParcelableExtra(EXTRA_USER_REPORT) as? UserReportViewModel
    private fun getReportDateFromExtra() = intent.getSerializableExtra(EXTRA_REPORT_DATE) as Calendar
}
