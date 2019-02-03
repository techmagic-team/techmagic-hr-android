package co.techmagic.hr.presentation.util

class TimeFormatUtil {
    companion object {
        fun formatMinutesToHours(minutes: Int) = String.format("%d:%02d", minutes / 60, minutes % 60)
    }
}