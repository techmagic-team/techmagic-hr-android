package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
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
    var underlineColor: Int = 0xFFE0004D.toInt()
    var selectedDay: Day = Day.NONE
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

    private var underlineWidth: Int = 0
    private var underlinePaint: Paint = Paint()

    private val monday: WeekdayView
    private val tuesday: WeekdayView
    private val wednesday: WeekdayView
    private val thursday: WeekdayView
    private val friday: WeekdayView
    private val saturday: WeekdayView
    private val sunday: WeekdayView

    init {
        View.inflate(context, R.layout.view_week, this)

        monday = find(R.id.monday)
        tuesday = find(R.id.tuesday)
        wednesday = find(R.id.wednesday)
        thursday = find(R.id.thursday)
        friday = find(R.id.friday)
        saturday = find(R.id.saturday)
        sunday = find(R.id.sunday)

        val weekdays = DateFormatSymbols().shortWeekdays
        monday.day = weekdays[Calendar.MONDAY]
        tuesday.day = weekdays[Calendar.TUESDAY]
        wednesday.day = weekdays[Calendar.WEDNESDAY]
        thursday.day = weekdays[Calendar.THURSDAY]
        friday.day = weekdays[Calendar.FRIDAY]
        saturday.day = weekdays[Calendar.SATURDAY]
        sunday.day = weekdays[Calendar.SUNDAY]

        underlinePaint.color = underlineColor
        underlinePaint.strokeWidth = dpToPx(4f, resources).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        underlineWidth = monday.measuredWidth
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawSelection(canvas)
    }

    private fun drawSelection(canvas: Canvas?) {
        if (selectedDay == Day.NONE) return

        val dayView = getChildAt(selectedDay.ordinal)
        val x = dayView.x + selectionOffset * dayView.width
        val y = height.toFloat() - underlinePaint.strokeWidth / 2
//        underlinePaint.color = todo: interpolate color
        canvas?.drawLine(x, y, x + underlineWidth.toFloat(), y, underlinePaint)
    }

    enum class Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY,
        NONE;

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
                    else -> NONE
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

    var day: String
        get() = tvDay.text.toString()
        set(value) {
            tvDay.text = value
        }

    var minutes: Int = 0
        set(value) {
            tvHours.text = String.format("%1d:%02d", value / 60, value % 60)
        }

    init {
        View.inflate(context, R.layout.view_week_day, this)
        tvDay = find(R.id.tvDay)
        tvHours = find(R.id.tvHours)
        minutes = 0
    }
}