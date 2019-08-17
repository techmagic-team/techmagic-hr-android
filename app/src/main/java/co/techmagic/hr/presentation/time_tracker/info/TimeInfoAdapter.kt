package co.techmagic.hr.presentation.time_tracker.info

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.util.TimeFormatUtil
import org.jetbrains.anko.find

class TimeReportViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val tvReportName: TextView = itemView.find(R.id.tvReportName)
    private val tvActual: TextView = itemView.find(R.id.tvActual)
    private val tvExpected: TextView = itemView.find(R.id.tvExpected)

    fun bind(model: TimeReportViewModel) {
        tvReportName.text = model.title
        tvActual.text = TimeFormatUtil.formatMinutesToHours(model.actualMinutes)
        tvExpected.text = TimeFormatUtil.formatMinutesToHours(model.expectedMinutes)

        tvActual.isSelected = model.actualMinutes != model.expectedMinutes
    }
}

class TimeInfoAdapter(private val reports: List<TimeReportViewModel>) : RecyclerView.Adapter<TimeReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): TimeReportViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_time_info_report, parent, false)
        return TimeReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeReportViewHolder, index: Int) {
        holder.bind(reports[index])
    }

    override fun getItemCount(): Int = reports.size
}