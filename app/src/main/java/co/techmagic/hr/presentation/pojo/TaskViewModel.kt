package co.techmagic.hr.presentation.pojo

import co.techmagic.hr.presentation.ui.adapter.headers_adapter.HasHeaderProperty

data class TaskViewModel(val id: String, val title: String) : HasHeaderProperty<String> {
    override fun getParent() = id
}