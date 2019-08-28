package co.techmagic.hr.presentation.time_tracker.info

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.util.TOOLBAR_DATE_FORMAT
import co.techmagic.hr.presentation.util.formatDate
import com.techmagic.viper.base.BaseViewFragment
import org.jetbrains.anko.find
import java.util.*

class TimeInfoFragment : BaseViewFragment<TimeInfoPresenter>(), TimeInfoView {

    companion object {
        private const val KEY_SELECTED_DATE = "KEY_SELECTED_DATE"

        fun newInstance(selectedDate: Calendar): TimeInfoFragment {
            val args = Bundle()
            args.putSerializable(KEY_SELECTED_DATE, selectedDate)
            val fragment = TimeInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var selectedDate: Calendar

    private lateinit var progress: ProgressBar
    private lateinit var rvInfo: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_info, container, false)
    }

    override fun initView() {
        super.initView()
        selectedDate = arguments?.get(KEY_SELECTED_DATE) as Calendar

        view?.also {
            it.find<View>(R.id.btnBack).setOnClickListener {
                activity?.onBackPressed()
            }

            it.find<TextView>(R.id.tvSelectedDate).text = selectedDate.formatDate(TOOLBAR_DATE_FORMAT)

            progress = it.find(R.id.progress)
            rvInfo = it.find(R.id.rvInfo)
            rvInfo.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
        rvInfo.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.INVISIBLE
        rvInfo.visibility = View.VISIBLE
    }

    override fun showReports(reports: List<WorkingTimeInfoViewModel>) {
        rvInfo.adapter = TimeInfoAdapter(reports)
    }
}
