package co.techmagic.hr.presentation.time_tracker

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.ui.adapter.TimeReportAdapter
import co.techmagic.hr.presentation.ui.adapter.TimeReportsClickListener
import java.util.*
import kotlin.math.abs

open class DayReportsAdapter(recyclerView: RecyclerView, anchorDate: Calendar) :
        DiscreteDateAdapter<DayReportViewHolder>(recyclerView, anchorDate, Step.DAY) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_day_reports, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(parent.measuredWidth, parent.measuredHeight)
        return DayReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayReportViewHolder, position: Int) {
        //TODO: implement
        when (abs(position % 3)) {
            0 -> holder.itemView.setBackgroundColor(Color.RED)
            1 -> holder.itemView.setBackgroundColor(Color.GREEN)
            2 -> holder.itemView.setBackgroundColor(Color.BLUE)
        }
    }
}

class DayReportViewHolder(view: View) : RecyclerView.ViewHolder(view), TimeTrackerDayView {
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

        reportsAdapter = TimeReportAdapter(context, object : TimeReportsClickListener {

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