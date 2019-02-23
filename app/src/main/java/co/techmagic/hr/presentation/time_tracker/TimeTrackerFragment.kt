package co.techmagic.hr.presentation.time_tracker

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import co.techmagic.hr.presentation.ui.view.WeekView
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.now
import com.techmagic.viper.base.BaseViewFragment
import java.util.*

class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {

    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    private lateinit var actionBarChangeListener: ActionBarChangeListener

    lateinit var weeksAdapter: WeeksAdapter

    lateinit var daysAdapter: DayReportsAdapter

    lateinit var btnAddTimeReport: CardView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarChangeListener = context as ActionBarChangeListener

        btnAddTimeReport = view.findViewById(R.id.btnAddTimeReport)

        var weeks: RecyclerView = view.findViewById(R.id.rvWeeks)
        weeks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val calendar = now()
        calendar.firstDayOfWeek = Calendar.MONDAY
        val anchor1 = calendar.firstDayOfWeekDate()
        weeksAdapter = WeeksAdapter(weeks, anchor1)
        weeks.adapter = weeksAdapter

        var days: RecyclerView = view.findViewById(R.id.rvDays)
        days.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val anchor2 = calendar
        daysAdapter = object : DayReportsAdapter(days, anchor2) {
            override fun onBindViewHolder(holder: DayReportViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                getPresenter()?.onBindDay(holder, pageToDate(position))
            }
        }
        days.adapter = daysAdapter


        daysAdapter.listener = object : DiscreteDateAdapter.OnDateChangeListener {
            override fun onDateSelected(date: Calendar) {
                weeks.smoothScrollToPosition(weeksAdapter.dateToPage(date))
                getPresenter()?.onDateSelected(date)
            }

            override fun onDateOffsetChanged(date: Calendar, offset: Float) {
                val currentWeekView = weeksAdapter.pagerSnapHelper.findSnapView(weeks.layoutManager)
                val viewHolder = weeks.findContainingViewHolder(currentWeekView)
                when (viewHolder) {
                    is WeekViewHolder -> {
                        viewHolder.weekView.selectedDay = WeekView.Day.from(date)
                        viewHolder.weekView.selectionOffset = offset
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_time_tracker, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_current_day -> {
                getPresenter()?.onCurrentDayClicked()
                true
            }
            R.id.menu_item_info -> {
                getPresenter()?.onInfoClicked()
                true
            }
            R.id.menu_item_calendar -> {
                getPresenter()?.onCalendarClicked()
                true
            }
            else -> false
        }
    }

    override fun selectDay(date: Calendar) {
    }

    override fun showToolbarTitle(title: String) {
        actionBarChangeListener.setActionBarTitle(title)
    }
}