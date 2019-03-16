package co.techmagic.hr.presentation.util

import android.annotation.SuppressLint
import android.support.annotation.IntRange
import java.text.SimpleDateFormat
import java.util.*

const val ISO_DATE_FORMAT = "yyyy-MM-dd"

fun now(): Calendar {
    return Calendar.getInstance()
}

const val TOOLBAR_DATE_FORMAT = "EEEE, dd 'of' MMM"

fun today(): Calendar {
    return now().dateOnly()
}

fun calendar(@IntRange(from = 1, to = 12) month: Int,
             @IntRange(from = 1900) year: Int): Calendar {
    return calendar(1, month, year)
}

fun calendar(@IntRange(from = 1, to = 31) day: Int,
             @IntRange(from = 1, to = 12) month: Int,
             @IntRange(from = 1900) year: Int): Calendar {
    val date = Calendar.getInstance()
    date.set(year, month - 1, day, 0, 0, 0)
    date.set(Calendar.MILLISECOND, 0)
    return date
}

fun Date.toCalendar(firstDayOfWeek: Int = Calendar.MONDAY): Calendar {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.firstDayOfWeek = firstDayOfWeek
    return instance
}

fun Calendar.copy(): Calendar {
    return this.clone() as Calendar
}

fun Calendar.nowMillis(): Long {
    return now().timeInMillis
}

fun Calendar.dateOnly(): Calendar {
    val date = copy()
    with(date) {
        clear(Calendar.HOUR)
        clear(Calendar.HOUR_OF_DAY)
        clear(Calendar.MINUTE)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
        set(Calendar.AM_PM, Calendar.AM)
    }
    return date
}

fun Calendar.firstDayOfWeekDate(): Calendar {
    val date = dateOnly()
    val weekFirstDay = date.firstDayOfWeek
    var dayOfWeek = date.get(Calendar.DAY_OF_WEEK)
    while (dayOfWeek != weekFirstDay) {
        date.add(Calendar.DAY_OF_MONTH, -1)
        dayOfWeek = date.get(Calendar.DAY_OF_WEEK)
    }
    return date
}

@SuppressLint("SimpleDateFormat")
fun Calendar.formatDate(pattern: String = ISO_DATE_FORMAT, locale: Locale? = null, timeZone: TimeZone = TimeZone.getDefault()): String {
    val format = if (locale != null) SimpleDateFormat(pattern, locale) else SimpleDateFormat(pattern)
    format.timeZone = timeZone
    return format.format(time)
}

fun Calendar.isSameDate(date: Calendar): Boolean {
    return get(Calendar.YEAR) == date.get(Calendar.YEAR)
            && get(Calendar.MONTH) == date.get(Calendar.MONTH)
            && get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
}