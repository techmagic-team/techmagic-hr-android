package co.techmagic.hr.presentation.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import co.techmagic.hr.presentation.pojo.TimeReportViewModel
import co.techmagic.hr.R
import co.techmagic.hr.presentation.util.TimeFormatUtil

class TimeTrackingAdapter : RecyclerView.Adapter<TimeTrackingAdapter.TimeTrackingViewHolder>() {

    private var data = arrayListOf<TimeReportViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTrackingViewHolder {
        //return TimeTrackingViewHolder(LayoutInflater.from(parent.context).)
        return TimeTrackingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_report, parent, true))
    }

    override fun onBindViewHolder(holder: TimeTrackingViewHolder, position: Int) {
        val timeReportItem = data[position]

        holder.tvTimeReportClient.text = timeReportItem.client
        holder.tvTimeReportProject.text = timeReportItem.project
        holder.tvTimeReportTask.text = timeReportItem.task
        holder.tvTimeReportNote.text = timeReportItem.note
        holder.tvTimeReportTime.text = TimeFormatUtil.formatMinutesToHours(timeReportItem.time)
        holder.imgTimeReportTrackTime.setImageDrawable(getReportActionImage(
                holder.imgTimeReportTrackTime.context,
                timeReportItem.isLocked,
                timeReportItem.isCurrentlyTracking)
        )
    }

    private fun getReportActionImage(context: Context,
                                     isLocked: Boolean,
                                     isCurrentlyTracking: Boolean): Drawable {
        @DrawableRes var actionImageRes: Int
        if (isLocked) {
            actionImageRes = R.drawable.ic_tracking_locked
        } else if (isCurrentlyTracking) {
            actionImageRes = R.drawable.ic_tracking_stop
        } else {
            actionImageRes = R.drawable.ic_tracking_stop
        }

        return ContextCompat.getDrawable(context, actionImageRes)!!
    }

    override fun getItemCount() = data.size

    public fun setNewData(newData: List<TimeReportViewModel>) {
        this.data.clear()
        this.data.addAll(newData)
    }

    public fun addReport(timeReport: TimeReportViewModel) = data.add(timeReport)

    class TimeTrackingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTimeReportClient: TextView = itemView.findViewById(R.id.tvTimeReportClient)
        val tvTimeReportProject: TextView = itemView.findViewById(R.id.tvTimeReportProject)
        val tvTimeReportTask: TextView = itemView.findViewById(R.id.tvTimeReportTask)
        val tvTimeReportNote: TextView = itemView.findViewById(R.id.tvTimeReportNote)
        val tvTimeReportTime: TextView = itemView.findViewById(R.id.tvTimeReportTime)
        val imgTimeReportTrackTime: ImageView = itemView.findViewById(R.id.imgTimeReportTrackTime)

    }
}
