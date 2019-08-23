package co.techmagic.hr.presentation.time_tracker.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import co.techmagic.hr.RepositoriesProvider
import co.techmagic.hr.presentation.util.HrAppDateTimeProvider
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import com.techmagic.viper.base.BasePresenter
import java.util.*

class TimeInfoActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SELECTED_DATE = "EXTRA_SELECTED_DATE"

        fun start(context: Context, selectedDate: Calendar) {
            val intent = Intent(context, TimeInfoActivity::class.java)
            intent.putExtra(EXTRA_SELECTED_DATE, selectedDate)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_months_info)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, TimeInfoFragment.newInstance(selectedDate))
                    .commitAllowingStateLoss()
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is TimeInfoFragment -> {
                val repository = (application as RepositoriesProvider).provideTimeReportRepository()
                val userId = SharedPreferencesUtil.readUser().id  // TODO: refactor - inject account manager instead!!!
                val presenter = HrAppTimeInfoPresenter(selectedDate, userId, repository)
                BasePresenter.bind(fragment, presenter, object : TimeInfoRouter {})
            }
        }
    }

    private val selectedDate: Calendar
        get() = intent.getSerializableExtra(EXTRA_SELECTED_DATE) as Calendar
}
