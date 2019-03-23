package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R

class ChooseReportPropertyAdapter<T : ReportProperty>
    : RecyclerView.Adapter<ChooseReportPropertyAdapter.ChooseReportProjectViewHolder<T>>() {

    private var data = arrayListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseReportProjectViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report_property, parent, false)
        return ChooseReportProjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChooseReportProjectViewHolder<T>, position: Int) {
        holder.bind(data[position])
    }

    public fun setData(data: List<T>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? {
        return if (position >= 0 && position < data.size) {
            data[position]
        } else {
            null
        }
    }

    class ChooseReportProjectViewHolder<T : ReportProperty>(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.tvReportPropertyTitle)

        public fun bind(property: T) {
            title.text = property.getTitle()
            title.isClickable = true
        }
    }
}