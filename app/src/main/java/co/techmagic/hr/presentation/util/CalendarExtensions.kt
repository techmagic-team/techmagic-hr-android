package co.techmagic.hr.presentation.util

import android.annotation.SuppressLint
import android.support.annotation.IntRange
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val ISO_DATE_FORMAT = "yyyy-MM-dd"
const val TOOLBAR_DATE_FORMAT = "EEE, dd 'of' MMM"
const val ISO_WITH_TIME_ZONE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"


fun now(): Calendar {
    return Calendar.getInstance()
}

fun today(): Calendar {
    return now().dateOnly()
}

fun daysBetween(date1: Calendar, date2: Calendar): Long {
    return TimeUnit.DAYS.convert(date1.timeInMillis, TimeUnit.MILLISECONDS) - TimeUnit.DAYS.convert(date2.timeInMillis, TimeUnit.MILLISECONDS)
}

fun Calendar.addDays(n: Int): Calendar {
    return this.copy().also {
        it.add(Calendar.DAY_OF_MONTH, n)
    }
}

fun Calendar.previousDay(): Calendar {
    return this.addDays(-1)
}

fun Calendar.previousWeek(): Calendar {
    return this.copy().also {
        it.add(Calendar.WEEK_OF_MONTH, -1)
    }
}

fun calendar(date: Date): Calendar {
    val calendarDate = Calendar.getInstance()
    calendarDate.time = date
    return calendarDate
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

fun Calendar.nextWeek(): Calendar {
    return this.firstDayOfWeekDate().also {
        it.add(Calendar.WEEK_OF_MONTH, 1)
    }
}

fun Calendar.firstDayOfMonthDate(): Calendar {
    return this.copy().also {
        val day = it.get(Calendar.DAY_OF_MONTH)
        it.add(Calendar.DAY_OF_MONTH, -day + 1)
    }
}

fun Calendar.lastDayOfMonthDate(): Calendar {
    return this.firstDayOfMonthDate()
            .also {
                it.add(Calendar.MONTH, 1)
            }.previousDay()
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