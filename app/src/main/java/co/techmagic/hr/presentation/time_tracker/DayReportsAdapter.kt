package co.techmagic.hr.presentation.time_tracker

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.ui.adapter.TimeReportAdapter
import co.techmagic.hr.presentation.ui.adapter.TimeReportsClickListener
import java.util.*

abstract class DayReportsAdapter(val timeReportsClickListener: TimeReportsClickListener, recyclerView: RecyclerView, anchorDate: Calendar) :
        DiscreteDateAdapter<DayReportViewHolder>(recyclerView, anchorDate, Step.DAY) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_day_reports, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(parent.measuredWidth, parent.measuredHeight)
        return DayReportViewHolder(timeReportsClickListener, view)
    }
}

class DayReportViewHolder(val timeReportsClickListener: TimeReportsClickListener, view: View) : RecyclerView.ViewHolder(view), TimeTrackerDayView {
    private lateinit var rvReports: RecyclerView
    private lateinit var llEmptyListContainer: ViewGroup
    private lateinit var tvEmptyListText: TextView

    private lateinit var reportsAdapter: TimeReportAdapter

    init {
        initRecycler()
    }

    override fun showReports(reports: List<UserReportViewModel>) {
        rvReports.visibility = View.VISIBLE
        llEmptyListContainer.visibility = View.GONE
        reportsAdapter.setNewData(reports)
    }

    override fun showEmptyMessage(quote: String) {
        rvReports.visibility = View.GONE
        llEmptyListContainer.visibility = View.VISIBLE
        tvEmptyListText.text = quote
    }

    private fun initRecycler() {
        val context = itemView.context
        rvReports = itemView.findViewById(R.id.rvReports)
        llEmptyListContainer = itemView.findViewById(R.id.llEmptyViewContainer)
        tvEmptyListText = itemView.findViewById(R.id.tvEmptyList)

        reportsAdapter = TimeReportAdapter(context, timeReportsClickListener)

        rvReports.adapter = reportsAdapter

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_time_reports)!!)

        rvReports.addItemDecoration(dividerItemDecoration)
    }
}