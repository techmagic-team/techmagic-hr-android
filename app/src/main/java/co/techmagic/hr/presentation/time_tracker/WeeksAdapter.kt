package co.techmagic.hr.presentation.time_tracker

import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import co.techmagic.hr.R
import co.techmagic.hr.presentation.time_tracker.Holiday.*
import co.techmagic.hr.presentation.ui.view.WeekView
import java.util.*

abstract class WeeksAdapter(recyclerView: RecyclerView, firstDayOfWeek: Calendar) :
        DiscreteDateAdapter<WeekViewHolder>(recyclerView, firstDayOfWeek, Step.WEEK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val weekView = WeekView(parent.context)
        weekView.layoutParams = ViewGroup.LayoutParams(parent.measuredWidth, WRAP_CONTENT)
        return WeekViewHolder(weekView)
    }
}

class WeekViewHolder(val weekView: WeekView) : RecyclerView.ViewHolder(weekView), TimeTrackerWeekView {
    override fun setSelectedDay(selectedDate: Calendar, isToday: Boolean) {
        weekView.selectedDay = WeekView.Day.from(selectedDate)
    }

    override fun setTotalTime(date: Calendar, minutes: Int, holiday: Holiday?) {
        val weekdayView = weekView[WeekView.Day.from(date)] ?: return
        weekdayView.minutes = minutes
        when (holiday) {
            null -> {
                if (!weekdayView.isHoliday) return
                weekdayView.holidayImageResource = null
                weekdayView.setBackgroundColor(Color.WHITE)
            }
            else -> {
                weekdayView.holidayImageResource = getHolidayIconRes(holiday)
                weekdayView.setBackgroundResource(getHolidayColorRes(holiday))
            }
        }
    }

    @ColorRes
    private fun getHolidayColorRes(holiday: Holiday): Int {
        return when (holiday) {
            EASTER, NEW_YEAR, INDEPENDENCE_DAY -> R.color.dull_pink
            DEFENDERS_DAY, PENTECOST, WOMEN_DAY -> R.color.macaroni_and_cheese
            CHRISTMAS, CONSTITUTION_DAY, OTHER -> R.color.sickly_yellow
        }
    }

    @DrawableRes
    private fun getHolidayIconRes(holiday: Holiday): Int {
        return when (holiday) {
            CHRISTMAS -> R.drawable.ic_christmas
            CONSTITUTION_DAY -> R.drawable.ic_constitution_day
            DEFENDERS_DAY -> R.drawable.ic_defenders_day
            EASTER -> R.drawable.ic_easter
            INDEPENDENCE_DAY -> R.drawable.ic_independence_day
            NEW_YEAR -> R.drawable.ic_new_year
            PENTECOST -> R.drawable.ic_pentecost
            WOMEN_DAY -> R.drawable.ic_womens_day
            OTHER -> R.drawable.ic_default_holiday
        }
    }
}