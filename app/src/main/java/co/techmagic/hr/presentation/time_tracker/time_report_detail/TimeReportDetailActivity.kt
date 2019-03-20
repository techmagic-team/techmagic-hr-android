package co.techmagic.hr.presentation.time_tracker.time_report_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.techmagic.hr.R
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.techmagic.viper.base.BasePresenter

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

    private fun init(){
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
        }
    }

    private fun replaceTimeReportDetailFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, TimeReportDetailFragment.newInstance())
                .commit()
    }
}
