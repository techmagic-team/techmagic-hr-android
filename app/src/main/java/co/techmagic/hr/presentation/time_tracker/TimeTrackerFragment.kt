package co.techmagic.hr.presentation.time_tracker

import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.ListenablePagerSnapHelper
import com.techmagic.viper.base.BaseViewFragment

class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {
    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    lateinit var btnAddTimeReport: CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddTimeReport = view.findViewById(R.id.btnAddTimeReport)

        var weeks: RecyclerView = view.findViewById(R.id.rvWeeks)
        weeks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val weeksPagerSnapHelper = ListenablePagerSnapHelper()
        weeksPagerSnapHelper.attachToRecyclerView(weeks)
        weeks.adapter = WeeksAdapter()

        var days: RecyclerView = view.findViewById(R.id.rvDays)
        days.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val daysPagerSnapHelper = ListenablePagerSnapHelper()
        daysPagerSnapHelper.attachToRecyclerView(days)
        days.adapter = DayReportsAdapter()
    }
}