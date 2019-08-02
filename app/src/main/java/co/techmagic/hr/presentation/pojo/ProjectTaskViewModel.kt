package co.techmagic.hr.presentation.pojo

import co.techmagic.hr.data.entity.time_report.Task
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.HasHeaderProperty

data class ProjectTaskViewModel(val id: String, val task : Task) : HasHeaderProperty<String> {
    override fun getParent() = task.id
}