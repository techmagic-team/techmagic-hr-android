package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter

import android.view.View
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.BaseHeadersAdapter

class TasksAdapter : BaseHeadersAdapter<String, ProjectTaskViewModel, TaskViewHolder>() {

    override fun getItemLayout(viewType: Int) = R.layout.item_report_property

    override fun createViewHolder(viewType: Int, view: View) = TaskViewHolder(view)

}

class TaskViewHolder(view: View) : BaseHeadersAdapter.BaseHeadersAdapterViewHolder<ProjectTaskViewModel>(view) {
    val title:TextView = itemView.findViewById(R.id.tvReportPropertyTitle)

    override fun bind(property: ProjectTaskViewModel) {
        title.text = property.task.name
    }
}