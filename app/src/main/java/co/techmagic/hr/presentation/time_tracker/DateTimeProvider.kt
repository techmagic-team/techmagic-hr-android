package co.techmagic.hr.presentation.time_tracker

import java.util.*

interface DateTimeProvider {
    fun now(): Calendar
}