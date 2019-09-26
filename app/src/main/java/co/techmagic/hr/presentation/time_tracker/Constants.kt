package co.techmagic.hr.presentation.time_tracker

import java.util.concurrent.TimeUnit


object Constants {
    /**
     * Total amount of hours that employee is supposed to work during the day.
     */
    val EXPECTED_MINUTES_PER_DAY = TimeUnit.HOURS.toMinutes(8).toInt()
}