package co.techmagic.hr.presentation.time_tracker.time_report_detail.report_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.pojo.ProjectTaskViewModel
import co.techmagic.hr.presentation.pojo.ProjectViewModel
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.BaseHeaderItemDecorator
import co.techmagic.hr.presentation.ui.adapter.headers_adapter.HasHeaderProperty

class ReportPropertyHeaderItemDecorator<T : HasHeaderProperty<*>> : BaseHeaderItemDecorator<T, TextView>() {

    override fun fillUpHeader(view: TextView, item: T) {
        when (item) {
            is ProjectViewModel -> view.text = item.client!!.name
            is ProjectTaskViewModel -> view.text = item.task.name
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }

    override fun createHeader(parent: ViewGroup) =
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_report_property_header, parent, false)
                    as TextView

}