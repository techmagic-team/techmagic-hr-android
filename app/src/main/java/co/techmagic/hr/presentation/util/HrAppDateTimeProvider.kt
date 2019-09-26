package co.techmagic.hr.presentation.util

import co.techmagic.hr.presentation.time_tracker.DateTimeProvider
import java.util.*

class HrAppDateTimeProvider : DateTimeProvider {
    override fun now(): Calendar {
        val now = Calendar.getInstance()
        now.firstDayOfWeek = Calendar.MONDAY
        return now
    }

}