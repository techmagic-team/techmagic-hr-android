package co.techmagic.hr.presentation.ui.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.presenter.TimeTrackerPresenter
import co.techmagic.hr.presentation.mvp.view.TimeTrackerView
import co.techmagic.hr.presentation.mvp.view.impl.TimeTrackerViewImpl
import co.techmagic.hr.presentation.pojo.TimeReportViewModel
import co.techmagic.hr.presentation.ui.adapter.TimeReportAdapter
import co.techmagic.hr.presentation.ui.adapter.TimeReportsClickListener

class TimeTrackerFragment : BaseFragment<TimeTrackerView, TimeTrackerPresenter>() {

    lateinit var rvReports: RecyclerView

    private lateinit var reportsAdapter: TimeReportAdapter

    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    override fun initView(): TimeTrackerView {
        return object : TimeTrackerViewImpl(this, activity!!.findViewById(android.R.id.content)) {
            override fun showReports(reports: List<TimeReportViewModel>) {
                reportsAdapter.setNewData(reports)
            }

        }
    }

    override fun initPresenter() = TimeTrackerPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_time_tracker, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvReports = view.findViewById(R.id.rvReports)//todo should I resolve ButterKnife code generation with kotlin issue

        initRecycler()

        presenter.test()
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