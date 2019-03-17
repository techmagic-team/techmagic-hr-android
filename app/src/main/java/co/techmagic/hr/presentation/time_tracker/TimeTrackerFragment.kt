package co.techmagic.hr.presentation.time_tracker

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import co.techmagic.hr.presentation.ui.view.WeekView
import co.techmagic.hr.presentation.util.copy
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import com.techmagic.viper.base.BaseViewFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.jetbrains.anko.find
import java.util.*


class TimeTrackerFragment : BaseViewFragment<TimeTrackerPresenter>(), TimeTrackerView {

    companion object {
        fun newInstance(): TimeTrackerFragment = TimeTrackerFragment()
        private const val TIME_TRACKER_FRAGMENT = "TimeTrackerFragment"
    }

    private lateinit var weeks: RecyclerView
    private lateinit var days: RecyclerView
    private lateinit var btnAddTimeReport: CardView
    private lateinit var actionBarChangeListener: ActionBarChangeListener

    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var daysAdapter: DayReportsAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        initClicks()
        super.onViewCreated(view, savedInstanceState)
        actionBarChangeListener = context as ActionBarChangeListener
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

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun findViews(view: View) {
        weeks = view.find(R.id.rvWeeks)
        days = view.find(R.id.rvDays)
        btnAddTimeReport = view.find(R.id.btnAddTimeReport)
    }

    private fun initClicks(){
        btnAddTimeReport.setOnClickListener { presenter?.onNewTimeReportClicked() }
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
                    presenter?.onDateSelected(date)
                    selectDayInWeeks(date)
                }
                presenter?.onBindWeek(holder, firstDayOfWeek)
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
                presenter?.onWeekSelected(date)
            }

            override fun onDateOffsetChanged(date: Calendar, offset: Float) {
            }
        }
    }

    private fun initDays(today: Calendar) {
        days.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        daysAdapter = object : DayReportsAdapter(days, today) {
            override fun onBindViewHolder(holder: DayReportViewHolder, position: Int) {
                presenter?.onBindDay(holder, pageToDate(position))
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
                presenter?.onDateSelected(date)
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
                ?: return null
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_time_tracker, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_current_day -> {
                presenter?.onCurrentDayClicked()
                true
            }
            R.id.menu_item_info -> {
                presenter?.onInfoClicked()
                true
            }
            R.id.menu_item_calendar -> {
                presenter?.onCalendarClicked()
                true
            }
            else -> false
        }
    }

    override fun showToolbarTitle(title: String) {
        actionBarChangeListener.setActionBarTitle(title)
    }

    fun showDatePicker(date: Calendar) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            presenter?.onDateSelected(year, monthOfYear, dayOfMonth)
        }
        val datePickerDialog = DatePickerDialog.newInstance(
                dateSetListener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show(activity?.fragmentManager, TIME_TRACKER_FRAGMENT)
    }
}