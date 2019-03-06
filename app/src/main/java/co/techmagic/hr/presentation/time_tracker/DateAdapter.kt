package co.techmagic.hr.presentation.time_tracker

import android.support.v7.widget.RecyclerView
import android.util.Log
import co.techmagic.hr.presentation.ui.view.EndlessAdapter
import co.techmagic.hr.presentation.ui.view.ListenablePagerSnapHelper
import co.techmagic.hr.presentation.util.copy
import co.techmagic.hr.presentation.util.dateOnly
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.formatDate
import java.util.*
import java.util.concurrent.TimeUnit

abstract class DiscreteDateAdapter<VH : RecyclerView.ViewHolder>(
        recyclerView: RecyclerView,
        anchorDate: Calendar,
        private val step: Step) : EndlessAdapter<VH>() {

    companion object {
        val dayInMillis = TimeUnit.DAYS.toMillis(1)
        val weekInMillis = dayInMillis * 7
    }

    @Suppress("JoinDeclarationAndAssignment")
    private val anchorDate: Calendar
    val pagerSnapHelper: ListenablePagerSnapHelper = ListenablePagerSnapHelper()

    var listener: OnDateChangeListener? = null

    init {
        this.anchorDate = anchorDate.dateOnly()
        pagerSnapHelper.attachToRecyclerView(recyclerView)
        pagerSnapHelper.addOnPageChangeListener(object : ListenablePagerSnapHelper.OnPageChangeListener {
            override fun onPageSelected(page: Int) {
                val date = pageToDate(page)
                Log.d("DateAdapter", "offset = ${page - centerIndex} Selected date: ${date.formatDate()}")
                listener?.onDateSelected(date)
            }

            override fun onPageOffsetChanged(page: Int, offset: Float) {
                Log.d("DateAdapter", "Page = $page with offset = $offset")
                listener?.onDateOffsetChanged(pageToDate(page), offset)
            }
        })
    }

    fun pageToDate(page: Int): Calendar {
        val offset = page - centerIndex
        val date = anchorDate.copy()
        date.add(step.calendarField, offset)
        return date
    }

    fun dateToPage(date: Calendar): Int {
        return centerIndex + when (step) {
            Step.DAY -> (date.timeInMillis - anchorDate.timeInMillis) / dayInMillis
            Step.WEEK -> {
                val diff = TimeUnit.DAYS.convert(date.firstDayOfWeekDate().timeInMillis, TimeUnit.MILLISECONDS) - TimeUnit.DAYS.convert(anchorDate.timeInMillis, TimeUnit.MILLISECONDS)
                val offset = diff / 7
                offset
            }
        }.toInt()
    }

    enum class Step(val calendarField: Int) {
        DAY(Calendar.DAY_OF_YEAR),
        WEEK(Calendar.WEEK_OF_YEAR)
    }

    interface OnDateChangeListener {
        fun onDateSelected(date: Calendar)
        fun onDateOffsetChanged(date: Calendar, offset: Float)
    }
}