package co.techmagic.hr.presentation.time_tracker

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import co.techmagic.hr.presentation.ui.view.EndlessAdapter
import co.techmagic.hr.presentation.ui.view.WeekView

class WeeksAdapter: EndlessAdapter<WeekViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val weekView = WeekView(parent.context)
        weekView.layoutParams = ViewGroup.LayoutParams(parent.measuredWidth, WRAP_CONTENT)
        return WeekViewHolder(weekView)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        //TODO: implement
    }
}

class WeekViewHolder(val weekView: WeekView): RecyclerView.ViewHolder(weekView)