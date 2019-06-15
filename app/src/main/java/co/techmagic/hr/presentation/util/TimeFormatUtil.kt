package co.techmagic.hr.presentation.util

import java.util.regex.Pattern

class TimeFormatUtil {
    companion object {
        const val TIME_PATTERN = "\\d{1,2}:\\d{1,2}"
        const val MINUTES_PATTERN = ":\\d{1,2}"
        const val HOURS_PATTERN = "\\d{1,2}:"
        fun formatMinutesToHours(minutes: Int) = String.format("%d:%02d", minutes / 60, minutes % 60)

        fun textToMinutes(inputedText: String) = ((getHours(inputedText)
                ?: 0) * 60) + (getMinutes(inputedText) ?: 0)

        fun getMinutes(text: String): Int? {
            val minutesMatcher = createMatcher(text, MINUTES_PATTERN)

            return if (minutesMatcher.find())
                minutesMatcher.group().drop(1).toInt()
            else
                null
        }

        fun getHours(text: String): Int? {
            val hoursMatcher = createMatcher(text, HOURS_PATTERN)

            return if (hoursMatcher.find())
                hoursMatcher.group().dropLastWhile { it == ':' }.toInt()
            else
                null
        }

        fun matchesTime(text: String) = createMatcher(text, TIME_PATTERN).matches()

        private fun createMatcher(text: String, pattern: String) = Pattern
                .compile(pattern)
                .matcher(text)
    }
}