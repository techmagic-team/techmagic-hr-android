package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import co.techmagic.hr.R
import com.wdullaer.materialdatetimepicker.Utils.dpToPx
import org.jetbrains.anko.find
import java.text.DateFormatSymbols
import java.util.*

class WeekView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    var holidayColor: Int = 0xFFE0004D.toInt()
    var underlineColor: Int = 0xFF00b2a9.toInt()
    var selectedDay: Day? = null
        set(value) {
            field = value
            selectionOffset = 0f
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

    private val monday: WeekdayView
    private val tuesday: WeekdayView
    private val wednesday: WeekdayView
    private val thursday: WeekdayView
    private val friday: WeekdayView
    private val saturday: WeekdayView
    private val sunday: WeekdayView
    private val days: Map<Day, WeekdayView>

    init {
        View.inflate(context, R.layout.view_week, this)

        monday = find(R.id.monday)
        tuesday = find(R.id.tuesday)
        wednesday = find(R.id.wednesday)
        thursday = find(R.id.thursday)
        friday = find(R.id.friday)
        saturday = find(R.id.saturday)
        sunday = find(R.id.sunday)
        days = mapOf(Day.MONDAY to monday, Day.TUESDAY to tuesday, Day.WEDNESDAY to wednesday,
                Day.THURSDAY to thursday, Day.FRIDAY to friday, Day.SATURDAY to saturday,
                Day.SUNDAY to sunday)

        val weekdays = DateFormatSymbols().shortWeekdays
        for ((day, view) in days) {
            view.day = weekdays[day.toCalendar()]
            view.setOnClickListener { onDayClickListener?.onDayClicked(day) }
        }

        underlinePaint.color = underlineColor
        underlinePaint.strokeWidth = dpToPx(4f, resources).toFloat()
    }

    operator fun get(day: Day) = days[day]

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        underlineWidth = monday.measuredWidth
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawSelection(canvas)
    }

    private fun drawSelection(canvas: Canvas?) {
        val day = selectedDay ?: return
        val dayView = days[day] ?: getChildAt(0)
        val x = dayView.x + selectionOffset * dayView.width
        val y = height.toFloat() - underlinePaint.strokeWidth / 2
//        underlinePaint.color = todo: interpolate color
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

    interface OnDayClickListener {
        fun onDayClicked(day: Day)
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