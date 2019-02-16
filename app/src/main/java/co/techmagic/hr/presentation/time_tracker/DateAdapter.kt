package co.techmagic.hr.presentation.time_tracker

import android.support.v7.widget.RecyclerView
import android.util.Log
import co.techmagic.hr.presentation.ui.view.EndlessAdapter
import co.techmagic.hr.presentation.ui.view.ListenablePagerSnapHelper
import co.techmagic.hr.presentation.util.copy
import co.techmagic.hr.presentation.util.dateOnly
import co.techmagic.hr.presentation.util.toString
import java.util.*
import java.util.concurrent.TimeUnit

abstract class DiscreteDateAdapter<VH : RecyclerView.ViewHolder>(
        recyclerView: RecyclerView,
        anchorDate: Calendar,
        private val step: Step) : EndlessAdapter<VH>() {

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
                Log.d("DateAdapter", "offset = ${page - centerIndex} Selected date: ${date.toString("dd-MMM-YYYY")}")
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
            Step.DAY -> (date.timeInMillis - anchorDate.timeInMillis) / TimeUnit.DAYS.toMillis(1)
            Step.WEEK -> (date.timeInMillis - anchorDate.timeInMillis) / TimeUnit.DAYS.toMillis(7)
//            Step.MONTH -> diff(date, anchorDate, Calendar.MONTH)
//            Step.YEAR -> diff(date, anchorDate, Calendar.YEAR)
        }.toInt()
    }

//    private fun diff(date1: Calendar, date2: Calendar, field: Int): Int {
//        return if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
//            date1.get(field) - date2.get(field)
//        } else {
//            date1.get(field) - date2.get(field)
//        }
//    }

    enum class Step(val calendarField: Int) {
        DAY(Calendar.DAY_OF_YEAR),
        WEEK(Calendar.WEEK_OF_YEAR),
//        MONTH(Calendar.MONTH),
//        YEAR(Calendar.YEAR)
    }

    interface OnDateChangeListener {
        fun onDateSelected(date: Calendar)
        fun onDateOffsetChanged(date: Calendar, offset: Float)
    }
}