package co.techmagic.hr.presentation.time_tracker

import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.WeekView
import co.techmagic.hr.presentation.util.copy
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.isSameDate
import co.techmagic.hr.presentation.util.now
import com.techmagic.viper.base.BaseViewFragment
import org.jetbrains.anko.find
import java.util.*


class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {
    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
    }

    private lateinit var weeks: RecyclerView
    private lateinit var days: RecyclerView
    private lateinit var btnAddTimeReport: CardView

    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var daysAdapter: DayReportsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun init(today: Calendar) {
        initWeeks(today)
        initDays(today)
    }

    override fun selectWeek(date: Calendar) {
        weeks.smoothScrollToPosition(weeksAdapter.dateToPage(date))
    }

    override fun selectDay(date: Calendar) {
        days.scrollToPosition(daysAdapter.dateToPage(date))
        selectDayInWeeks(date)
    }

    override fun notifyWeekDataChanged(date: Calendar) {
        weeksAdapter.notifyItemChanged(weeksAdapter.dateToPage(date))
    }

    override fun notifyDayReportsChanged(date: Calendar) {
        daysAdapter.notifyItemChanged(daysAdapter.dateToPage(date))
    }

    private fun findViews(view: View) {
        weeks = view.find(R.id.rvWeeks)
        days = view.find(R.id.rvDays)
        btnAddTimeReport = view.find(R.id.btnAddTimeReport)
    }

    private fun initWeeks(today: Calendar) {
        weeks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        weeksAdapter = object : WeeksAdapter(weeks, today.firstDayOfWeekDate()) {
            override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
                val firstDayOfWeek = pageToDate(position)
                holder.weekView.weekStartDate = firstDayOfWeek
                holder.weekView.todayDate = today
                holder.weekView.onDayClickListener = { day ->
                    val date = firstDayOfWeek.copy()
                    date.add(Calendar.DAY_OF_WEEK, day.ordinal)
                    getPresenter()?.onDateSelected(date)
                }
                getPresenter()?.onBindWeek(holder, firstDayOfWeek)
            }
        }
        weeks.adapter = weeksAdapter

        weeks.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }

        weeksAdapter.listener = object : DiscreteDateAdapter.OnDateChangeListener {
            override fun onDateSelected(date: Calendar) {
                val weekView = findCurrentWeekView()
                val selectedDay = weekView?.selectedDay
                val selectedDayIndex = selectedDay?.ordinal ?: 0
                getPresenter()?.onWeekSelected(date, selectedDayIndex)
            }

            override fun onDateOffsetChanged(date: Calendar, offset: Float) {
            }
        }
    }

    private fun initDays(today: Calendar) {
        days.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        daysAdapter = object : DayReportsAdapter(days, today) {
            override fun onBindViewHolder(holder: DayReportViewHolder, position: Int) {
                getPresenter()?.onBindDay(holder, pageToDate(position))
            }
        }
        days.adapter = daysAdapter

        days.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }

        daysAdapter.listener = object : DiscreteDateAdapter.OnDateChangeListener {
            override fun onDateSelected(date: Calendar) {
                getPresenter()?.onDateSelected(date)
            }

            override fun onDateOffsetChanged(date: Calendar, offset: Float) {
                val weekView = findCurrentWeekView()
                weekView?.selectedDay = WeekView.Day.from(date)
                weekView?.selectionOffset = offset
            }
        }
    }

    private fun findCurrentWeekView(): WeekView? {
        val currentWeekView = weeksAdapter.pagerSnapHelper.findSnapView(weeks.layoutManager)
        val viewHolder = weeks.findContainingViewHolder(currentWeekView)
        return when (viewHolder) {
            is WeekViewHolder -> viewHolder.weekView
            else -> null
        }
    }

    private fun selectDayInWeeks(selectedDate: Calendar) {
        for (i in 0 until weeks.childCount) {
            val child = weeks.getChildAt(i)
            val vh = weeks.findContainingViewHolder(child)
            if (vh is WeekViewHolder) {
                vh.setSelectedDay(selectedDate)
            }
        }
    }
}