package co.techmagic.hr.presentation.util

import java.util.regex.Pattern

class TimeFormatUtil {
    companion object {
        const val MINUTES_PATTERN = ":\\d{1,2}"
        const val HOURS_PATTERN = "\\d{1,2}:"
        fun formatMinutesToHours(minutes: Int) = String.format("%d:%02d", minutes / 60, minutes % 60)

        fun textToMinutes(inputedText: String) = ((getHours(inputedText) ?: 0) * 60)+ (getMinutes(inputedText) ?: 0)

        fun getMinutes(text: String): Int? {
            val minutesMatcher = Pattern
                    .compile(MINUTES_PATTERN)
                    .matcher(text)

            return if (minutesMatcher.find())
                minutesMatcher.group().drop(1).toInt()
            else
                null
        }

        fun getHours(text: String): Int? {
            val hoursMatcher = Pattern
                    .compile(HOURS_PATTERN)
                    .matcher(text)
            return if (hoursMatcher.find())
                hoursMatcher.group().dropLastWhile { it == ':' }.toInt()
            else
                null
        }
    }
}