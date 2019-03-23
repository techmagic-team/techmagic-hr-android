package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter.Companion.PROJECT
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter.Companion.ReportProjectType
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.HrAppReportProjectPresenter.Companion.TASK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsFragment
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsFragment.Companion.ARG_FIRST_DAY_OF_WEEK
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsFragment.Companion.ARG_PROJECT_ID
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsFragment.Companion.ARG_TYPE
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsFragment.Companion.ARG_USER_ID
import co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.ReportProjectsRouter
import com.techmagic.viper.base.BasePresenter
import java.util.*

class TimeReportDetailActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, TimeReportDetailActivity::class.java))
        }
    }

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
                val timeReportDetailPresenter = HrAppTimeReportDetailPresenter()
                val timeReportRouter = TimeReportDetailRouter(this, fragment)
                BasePresenter.bind(fragment, timeReportDetailPresenter, timeReportRouter)
            }
            is ReportProjectsFragment -> {
                @ReportProjectType val type = fragment.arguments?.getInt(ARG_TYPE)
                val projectId = fragment.arguments?.getString(ARG_PROJECT_ID)
                val userId = fragment.arguments?.getString(ARG_USER_ID)
                val firstDayOfWeek = fragment.arguments?.getSerializable(ARG_FIRST_DAY_OF_WEEK) as? Date

                val reportProjectsPresenter = HrAppReportProjectPresenter()
                val reportProjectsRouter = ReportProjectsRouter(this)

                reportProjectsPresenter.type = type!!

                when (type) {
                    PROJECT -> reportProjectsPresenter.userId = userId
                    TASK -> {
                        reportProjectsPresenter.projectId = projectId
                        reportProjectsPresenter.firstDayOfWeek = firstDayOfWeek
                    }
                }

                BasePresenter.bind(fragment, reportProjectsPresenter, reportProjectsRouter)
            }
        }
    }

    private fun replaceTimeReportDetailFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, TimeReportDetailFragment.newInstance())
                .commit()
    }
}
