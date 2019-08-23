package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.techmagic.hr.R
import co.techmagic.hr.RepositoriesProvider
import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.repository.TimeReportNetworkRepository
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailFragment
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.BaseTimeReportDetailFragment.Companion.ARG_MINUTES_IN_DAY_EXCLUDE_THIS
import co.techmagic.hr.presentation.time_tracker.time_report_detail.base.HrAppBaseTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.create_report.CreateTimeReportFragment
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
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.TaskDetailViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.mapper.UserReportViewModelMapper
import co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report.HrAppUpdateTimeReportDetailPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.update_report.UpdateTimeReportFragment
import co.techmagic.hr.presentation.ui.manager.AccountManager
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import com.techmagic.viper.base.BasePresenter
import java.util.*


class TimeReportDetailActivity : AppCompatActivity(), ActionBarChangeListener {

    companion object {
        const val EXTRA_USER_REPORT = "extra_time_report"
        const val EXTRA_REPORT_DATE = "extra_report_date"
        const val EXTRA_OLD_ID = "EXTRA_OLD_ID"
        const val EXTRA_MINUTES_IN_DAY_EXCLUDE_THIS = "arg_minutes_in_day_exclude_this"

        fun start(fragment: Fragment,
                  userReportForEdit: UserReportViewModel?,
                  reportDate: Calendar,
                  requestCode: Int,
                  minutesInDayExcludedThis: Int) {
            val intent = Intent(fragment.activity, TimeReportDetailActivity::class.java)

            intent.putExtra(EXTRA_USER_REPORT, userReportForEdit)
            intent.putExtra(EXTRA_REPORT_DATE, reportDate)
            intent.putExtra(EXTRA_MINUTES_IN_DAY_EXCLUDE_THIS, minutesInDayExcludedThis)

            fragment.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var timeReportDetailPresenter: HrAppBaseTimeReportDetailPresenter<*>
    private lateinit var reportPropertiesPresenter: HrAppReportPropertiesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_report_detail)
        init()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceTimeReportDetailFragment(intent.getIntExtra(EXTRA_MINUTES_IN_DAY_EXCLUDE_THIS, 0))
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is CreateTimeReportFragment -> {
                subscribeOnBackStackChanges()
                inject(fragment)
            }
            is UpdateTimeReportFragment -> {
                subscribeOnBackStackChanges()
                inject(fragment)
            }
            is ReportPropertiesFragment -> {
                @ReportProjectType val type = fragment.arguments?.getInt(ARG_TYPE)
                val projectId = fragment.arguments?.getString(ARG_PROJECT_ID)
                val userId = fragment.arguments?.getString(ARG_USER_ID)
                val firstDayOfWeek = fragment.arguments?.getSerializable(ARG_FIRST_DAY_OF_WEEK) as? Calendar

                val okHttpClientClient = ApiClient.buildOkHttpClientClient()
                val retrofit = ApiClient.getRetrofit(okHttpClientClient)
                val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)
                val timeReportRepository = TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager(), AccountManager(applicationContext))

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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        (currentFragment as? BaseTimeReportDetailFragment<*>)?.onBackPressed()
                ?: super.onBackPressed()
    }

    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceTimeReportDetailFragment(alreadyReportedMinutesInDayWithoutCurrentMinutes: Int) {
        val fragment = if (isCreateReport()) {
            CreateTimeReportFragment.newInstance(alreadyReportedMinutesInDayWithoutCurrentMinutes)
        } else {
            UpdateTimeReportFragment.newInstance(alreadyReportedMinutesInDayWithoutCurrentMinutes)
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    private fun getUserReportForEdit(): UserReportViewModel? = intent.getParcelableExtra(EXTRA_USER_REPORT) as? UserReportViewModel
    private fun getReportDateFromExtra() = intent.getSerializableExtra(EXTRA_REPORT_DATE) as Calendar
    private fun isCreateReport() = getUserReportForEdit() == null

    private fun subscribeOnBackStackChanges() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            currentFragment?.userVisibleHint = true
        }
    }

    private fun inject(fragment: CreateTimeReportFragment) {
        timeReportDetailPresenter = provideCreateReportPresenter()
        timeReportDetailPresenter.alreadyReportedMinutesInDayWithoutCurrentMinutes = fragment.arguments?.getInt(ARG_MINUTES_IN_DAY_EXCLUDE_THIS) ?: 0
        BasePresenter.bind(fragment, timeReportDetailPresenter as HrAppCreateTimeReportDetailPresenter, provideTimeReportRouter(fragment))
    }

    private fun inject(fragment: UpdateTimeReportFragment) {
        timeReportDetailPresenter = provideUpdateReportPresenter()
        timeReportDetailPresenter.alreadyReportedMinutesInDayWithoutCurrentMinutes = fragment.arguments?.getInt(ARG_MINUTES_IN_DAY_EXCLUDE_THIS) ?: 0
        BasePresenter.bind(fragment, timeReportDetailPresenter as HrAppUpdateTimeReportDetailPresenter, provideTimeReportRouter(fragment))
    }

    private fun provideCreateReportPresenter(): HrAppCreateTimeReportDetailPresenter {
        val presenter = HrAppCreateTimeReportDetailPresenter(
                provideTimeReportRepository(),
                (application as RepositoriesProvider).run { provideTimeTrackerInteractor() },
                UserReportViewModelMapper(),
                ProjectViewModelMapper(),
                ProjectTaskViewModelMapper()
        )

        presenter.reportDate = getTimeReportDate()

        return presenter
    }

    private fun provideUpdateReportPresenter(): HrAppUpdateTimeReportDetailPresenter {
        val presenter = HrAppUpdateTimeReportDetailPresenter(
                provideTimeReportRepository(),
                UserReportViewModelMapper(),
                (application as RepositoriesProvider).run { provideTimeTrackerInteractor() },
                TaskDetailViewModelMapper(ProjectViewModelMapper())
        )

        presenter.userReportForEdit = getUserReportForEdit()
        presenter.reportDate = getTimeReportDate()

        return presenter
    }

    private fun provideTimeReportRepository(): TimeReportNetworkRepository {
        val okHttpClientClient = ApiClient.buildOkHttpClientClient()
        val retrofit = ApiClient.getRetrofit(okHttpClientClient)
        val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)

        return TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager(), AccountManager(applicationContext))
    }

    private fun provideTimeReportRouter(fragment: BaseTimeReportDetailFragment<*>): TimeReportDetailRouter {
        return TimeReportDetailRouter(this, fragment)
    }

    private fun getTimeReportDate(): Calendar {
        val timeReportDate = getReportDateFromExtra()
        timeReportDate.firstDayOfWeek = Calendar.MONDAY

        return timeReportDate
    }
}
