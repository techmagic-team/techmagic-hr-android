package co.techmagic.hr.presentation.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import co.techmagic.hr.presentation.pojo.TimeReportViewModel
import co.techmagic.hr.R
import co.techmagic.hr.presentation.util.*

class TimeReportAdapter(private val context: Context, private val timeReportsClickListener: TimeReportsClickListener)
    : RecyclerView.Adapter<TimeReportAdapter.TimeTrackingViewHolder>() {

    private var data = arrayListOf<TimeReportViewModel>()

    private val disabledTextColor = R.color.color_time_report_disabled_text

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTrackingViewHolder {
        return TimeTrackingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_report, parent, false))
    }

    override fun onBindViewHolder(holder: TimeTrackingViewHolder, position: Int) {
        val timeReportItem = data[position]
        val isLocked = timeReportItem.isLocked

        holder.tvTimeReportClient.text = timeReportItem.client
        holder.tvTimeReportProject.text = timeReportItem.project
        holder.tvTimeReportTask.text = timeReportItem.task
        holder.tvTimeReportNote.text = timeReportItem.note
        holder.tvTimeReportTime.text = TimeFormatUtil.formatMinutesToHours(timeReportItem.time)

        holder.tvTimeReportClient.setTextColorRes(if (isLocked) disabledTextColor else R.color.color_time_report_client_text_color)
        holder.tvTimeReportProject.setTextColorRes(if (isLocked) disabledTextColor else R.color.color_time_report_project_text_color)
        holder.tvTimeReportTask.setTextColorRes(if (isLocked) disabledTextColor else R.color.color_time_report_task_text_color)
        holder.tvTimeReportNote.setTextColorRes(if (isLocked) disabledTextColor else R.color.color_time_report_note_text_color)
        holder.tvTimeReportTime.setTextColorRes(if (isLocked) disabledTextColor else R.color.color_time_report_task_text_color)
        holder.ivTimeReportTrackTime.isEnabled = !isLocked

        if (isLocked) {
            holder.llTimeReportContainer.setBackgroundColorRes( R.color.color_time_report_list_item_disabled_background)
            holder.ivTimeReportTrackTime.setImageDrawableRes(R.drawable.ic_tracking_locked)
        } else {
            holder.llTimeReportContainer.setBackgroundDrawableRes( R.drawable.bg_report_item_enabled)
            holder.ivTimeReportTrackTime.setImageDrawableRes(
                    if (timeReportItem.isCurrentlyTracking) R.drawable.ic_tracking_stop else R.drawable.ic_tracking_start
            )
        }

        holder.llTimeReportContainer.setOnClickListener { timeReportsClickListener.onItemClicked(position) }
        holder.ivTimeReportTrackTime.setOnClickListener { timeReportsClickListener.onTrackTimeClicked(position) }
    }

    override fun getItemCount() = data.size

    fun setNewData(newData: List<TimeReportViewModel>) {
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }

    fun addReport(timeReport: TimeReportViewModel) {
        data.add(timeReport)
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
    fun onTrackTimeClicked(position: Int)

    fun onItemClicked(position: Int)
}
