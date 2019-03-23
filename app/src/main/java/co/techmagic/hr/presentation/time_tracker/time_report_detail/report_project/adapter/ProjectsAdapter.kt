package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter

import android.view.View
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.pojo.TaskViewModel
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.BaseHeadersAdapter


class ProjectsAdapter : BaseHeadersAdapter<String, ProjectViewModel, ProjectsViewHolder>() {

    override fun getItemLayout(viewType: Int) = R.layout.item_report_property

    override fun createViewHolder(viewType: Int, view: View) = ProjectsViewHolder(view)

}

class ProjectsViewHolder(view: View) : BaseHeadersAdapter.BaseHeadersAdapterViewHolder<ProjectViewModel>(view) {
    val title: TextView = itemView.findViewById(R.id.tvReportPropertyTitle)

    override fun bind(property: ProjectViewModel) {
        title.text = property.title
    }
}