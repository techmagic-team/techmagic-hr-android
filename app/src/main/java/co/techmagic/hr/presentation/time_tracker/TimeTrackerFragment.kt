package co.techmagic.hr.presentation.time_tracker

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.ui.adapter.TimeReportAdapter
import co.techmagic.hr.presentation.ui.adapter.TimeReportsClickListener
import com.techmagic.viper.base.BaseViewFragment

class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {
    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    lateinit var rvReports: RecyclerView

    lateinit var btnAddTimeReport: CardView
    private lateinit var reportsAdapter: TimeReportAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvReports = view.findViewById(R.id.rvReports)//todo should I resolve ButterKnife code generation with kotlin issue
        btnAddTimeReport = view.findViewById(R.id.btnAddTimeReport)

        initRecycler()
    }

    override fun showReports(reports: List<UserReportViewModel>) {
        reportsAdapter.setNewData(reports)
    }

    private fun initRecycler() {
        reportsAdapter = TimeReportAdapter(context!!, object : TimeReportsClickListener {

            override fun onTrackTimeClicked(position: Int) {
                Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
            }

            override fun onItemClicked(position: Int) {
                Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
            }

        })

        rvReports.adapter = reportsAdapter

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_time_reports)!!)

        rvReports.addItemDecoration(dividerItemDecoration)
    }
}