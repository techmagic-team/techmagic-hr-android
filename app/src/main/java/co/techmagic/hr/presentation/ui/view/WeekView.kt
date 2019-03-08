package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.view.WeekView.Day.*
import co.techmagic.hr.presentation.util.dateOnly
import co.techmagic.hr.presentation.util.firstDayOfWeekDate
import co.techmagic.hr.presentation.util.isSameDate
import com.wdullaer.materialdatetimepicker.Utils.dpToPx
import org.jetbrains.anko.find
import java.text.DateFormatSymbols
import java.util.*

typealias OnDayClickListener = ((day: WeekView.Day) -> Unit)

class WeekView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    var todayColor: Int = context.resources.getColor(R.color.ruby)
    var underlineColor: Int = context.resources.getColor(R.color.turquoise)

    var weekStartDate: Calendar? = null
        set(value) {
            field = value?.firstDayOfWeekDate()
        }
    var todayDate: Calendar? = null
    private val isToday: Boolean
        get() {
            val week = weekStartDate
            val today = todayDate
            val selected = selectedDay?.ordinal ?: -1
            if (selected >= 0 && week != null && today != null) {
                val selectedDay = week.dateOnly()
                selectedDay.add(Calendar.DAY_OF_WEEK, selected)
                return today.isSameDate(selectedDay)
            }
            return false
        }

    var selectedDay: Day? = null
        set(value) {
            if (field == value) return
            field = value
            selectionOffset = 0f
            Log.d("WeekView", "selected day is ${value.toString()} view hash = ${hashCode()}")
            invalidate()
        }
    var selectionOffset: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var onDayClickListener: OnDayClickListener? = null

    private var underlineWidth: Int = 0
    private var underlinePaint: Paint = Paint()

    private val days: Map<Day, WeekdayView>

    init {
        View.inflate(context, R.layout.view_week, this)
        days = mapOf(
                MONDAY to find(R.id.monday),
                TUESDAY to find(R.id.tuesday),
                WEDNESDAY to find(R.id.wednesday),
                THURSDAY to find(R.id.thursday),
                FRIDAY to find(R.id.friday),
                SATURDAY to find(R.id.saturday),
                SUNDAY to find(R.id.sunday)
        )

        val weekdays = DateFormatSymbols().shortWeekdays
        for ((day, view) in days) {
            view.day = weekdays[day.toCalendar()]
            view.setOnClickListener { onDayClickListener?.invoke(day) }
        }

        underlinePaint.color = underlineColor
        underlinePaint.strokeWidth = dpToPx(4f, resources).toFloat()
    }

    operator fun get(day: Day) = days[day]

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        underlineWidth = days[MONDAY]?.measuredWidth ?: 0
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawSelection(canvas)
    }

    private fun drawSelection(canvas: Canvas?) {
        val dayView = days[selectedDay] ?: return
        val x = dayView.x + selectionOffset * dayView.width
        val y = height.toFloat() - underlinePaint.strokeWidth / 2

        if (isToday) {
            underlinePaint.color = todayColor
        } else {
            underlinePaint.color = underlineColor
        }

        canvas?.drawLine(x, y, x + underlineWidth.toFloat(), y, underlinePaint)
    }

    enum class Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

        fun toCalendar(): Int {
            return when (this) {
                MONDAY -> Calendar.MONDAY
                TUESDAY -> Calendar.TUESDAY
                WEDNESDAY -> Calendar.WEDNESDAY
                THURSDAY -> Calendar.THURSDAY
                FRIDAY -> Calendar.FRIDAY
                SATURDAY -> Calendar.SATURDAY
                SUNDAY -> Calendar.SUNDAY
            }
        }

        companion object {
            fun from(date: Calendar): Day {
                return when (date[Calendar.DAY_OF_WEEK]) {
                    Calendar.MONDAY -> MONDAY
                    Calendar.TUESDAY -> TUESDAY
                    Calendar.WEDNESDAY -> WEDNESDAY
                    Calendar.THURSDAY -> THURSDAY
                    Calendar.FRIDAY -> FRIDAY
                    Calendar.SATURDAY -> SATURDAY
                    Calendar.SUNDAY -> SUNDAY
                    else -> MONDAY
                }
            }
        }
    }
}

class WeekdayView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr) {

    private val tvDay: TextView
    private val tvHours: TextView
    private val ivHolidayBg: ImageView

    var day: String
        get() = tvDay.text.toString()
        set(value) {
            tvDay.text = value
        }

    var minutes: Int = 0
        set(value) {
            tvHours.text = String.format("%1d:%02d", value / 60, value % 60)
            field = value
        }

    @DrawableRes
    var holidayImageResource: Int? = null
        set(resId) {
            if (resId != null) {
                ivHolidayBg.setImageResource(resId)
                ivHolidayBg.visibility = View.VISIBLE
            } else {
                ivHolidayBg.visibility = View.GONE
                ivHolidayBg.background = null
            }
            field = resId
        }

    var isHoliday: Boolean = holidayImageResource != null

    init {
        View.inflate(context, R.layout.view_week_day, this)
        tvDay = find(R.id.tvDay)
        tvHours = find(R.id.tvHours)
        ivHolidayBg = find(R.id.ivHolidayBg)
        minutes = 0
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        // TODO: update typeface
    }
}