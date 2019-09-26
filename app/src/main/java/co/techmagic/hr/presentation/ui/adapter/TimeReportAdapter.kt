package co.techmagic.hr.presentation.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.UserReportViewModel
import co.techmagic.hr.presentation.util.TimeFormatUtil
import co.techmagic.hr.presentation.util.setImageDrawableRes

class TimeReportAdapter(private val context: Context, private val timeReportsClickListener: TimeReportsClickListener)
    : RecyclerView.Adapter<TimeReportAdapter.TimeTrackingViewHolder>() {

    var data = arrayListOf<UserReportViewModel>()

    private val disabledTextColor = R.color.color_time_report_disabled_text

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTrackingViewHolder {
        return TimeTrackingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_report, parent, false))
    }

    override fun onBindViewHolder(holder: TimeTrackingViewHolder, position: Int) {
        val timeReportItem = data[position]
        val isLocked = timeReportItem.isLocked

        holder.tvTimeReportClient.text = timeReportItem.client
        holder.tvTimeReportProject.text = timeReportItem.project
        holder.tvTimeReportTask.text = timeReportItem.task.name
        holder.tvTimeReportNote.text = timeReportItem.note
        holder.tvTimeReportTime.text = TimeFormatUtil.formatMinutesToHours(timeReportItem.minutes)

        holder.tvTimeReportClient.isEnabled = !isLocked
        holder.tvTimeReportProject.isEnabled = !isLocked
        holder.tvTimeReportTask.isEnabled = !isLocked
        holder.tvTimeReportNote.isEnabled = !isLocked
        holder.tvTimeReportTime.isEnabled = !isLocked
        holder.ivTimeReportTrackTime.isEnabled = !isLocked
        holder.llTimeReportContainer.isEnabled = !isLocked
        if (isLocked) {
            holder.ivTimeReportTrackTime.setImageDrawableRes(R.drawable.ic_tracking_locked)
        } else {
            holder.ivTimeReportTrackTime.setImageDrawableRes(
                    if (timeReportItem.isCurrentlyTracking) R.drawable.ic_tracking_stop else R.drawable.ic_tracking_start
            )
        }

        holder.llTimeReportContainer.setOnClickListener { timeReportsClickListener.onItemClicked(timeReportItem) }
        holder.ivTimeReportTrackTime.setOnClickListener { timeReportsClickListener.onTrackTimeClicked(data[position]) }
    }

    override fun getItemCount() = data.size

    fun setData(newData: List<UserReportViewModel>) {
        this.data.clear()
        this.data.addAll(newData)
    }

    fun addReport(userReport: UserReportViewModel) {
        data.add(userReport)
        notifyItemInserted(data.size - 1)
    }

    class TimeTrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val llTimeReportContainer: LinearLayout = itemView.findViewById(R.id.llTimeReportContainer)
        val tvTimeReportClient: TextView = itemView.findViewById(R.id.tvTimeReportClient)
        val tvTimeReportProject: TextView = itemView.findViewById(R.id.tvTimeReportProject)
        val tvTimeReportTask: TextView = itemView.findViewById(R.id.tvTimeReportTask)
        val tvTimeReportNote: TextView = itemView.findViewById(R.id.tvTimeReportNote)
        val tvTimeReportTime: TextView = itemView.findViewById(R.id.tvTimeReportTime)
        val ivTimeReportTrackTime: ImageView = itemView.findViewById(R.id.ivTimeReportTrackTime)
    }
}

interface TimeReportsClickListener {
    fun onTrackTimeClicked(userReportViewModel: UserReportViewModel)

    fun onItemClicked(reportViewModel: UserReportViewModel)
}
