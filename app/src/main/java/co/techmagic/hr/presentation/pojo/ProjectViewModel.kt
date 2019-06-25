package co.techmagic.hr.presentation.pojo

import co.techmagic.hr.data.entity.time_tracker.Client
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.HasHeaderProperty

data class ProjectViewModel(val id: String, val title: String, val client: Client?) : HasHeaderProperty<String> {
    override fun getParent() = client?.name ?: ""
}
