package co.techmagic.hr.presentation.util

import android.text.Editable
import android.widget.EditText

open class TimeInputTextWatcher constructor(val editText: EditText) : SimpleTextWatcher() {

    private var isInnerChange = false

    companion object {
        const val MAX_HOURS = 23
        const val MAX_MINUTES = 59
        const val MIN_MINUTES = 0

        const val MAX_HOURS_LENGTH = 2
        const val MAX_MINUTES_LENGTH = 2

        const val START_MINUTES_CURSOR_POSITION = 3

        const val TIME_SEPARATOR = ":"
    }

    override fun afterTextChanged(s: Editable?) {
        s ?: return
        if (isInnerChange) return

        try {
            var text = s.toString()
            var hours = TimeFormatUtil.getHours(text) ?: -1
            var minutes = TimeFormatUtil.getMinutes(text) ?: -1

            isInnerChange = true

            if (!text.contains(TIME_SEPARATOR)) {
                text = text.substring(0, editText.selectionEnd) + ":" + text.substring(editText.selectionEnd, text.length)
                s.replace(0, s.length, text)
                editText.setSelection(s.indexOf(TIME_SEPARATOR))
            } else if (!isTextValid(text, hours, minutes)) {

                if (hours > MAX_HOURS) {
                    hours = MAX_HOURS
                    minutes = MAX_MINUTES
                } else if (minutes > MAX_MINUTES) {
                    minutes = MAX_MINUTES
                }

                val formattedText = getValidTime(hours, minutes)
                s.replace(0, s.length, formattedText)
                moveCursor(formattedText, hours, minutes, editText.selectionEnd)
            } else {
                moveCursor(text, hours, minutes, editText.selectionEnd)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            isInnerChange = false
        }

    }

    private fun isTextValid(text: String, hours: Int, minutes: Int): Boolean {
        return TimeFormatUtil.matchesTime(text) && TimeFormatUtil.isTimeValid(hours, minutes)
    }

    private fun getValidTime(hours: Int, minutes: Int) = String.format("%s:%s", if (hours != -1) hours else "", if (minutes != -1) minutes else "")

    private fun moveCursor(text: String, hours: Int, minutes: Int, currentCursorPosition: Int) {
        if (isHoursChange(hours, currentCursorPosition) && isHoursFilledUp(hours)) {
            if (isMinutesFillUpped(minutes)) {
                editText.setSelection(editText.text.length - minutes.toString().length, editText.length())
            } else {
                editText.setSelection(START_MINUTES_CURSOR_POSITION)
            }
        } else if (isHoursFilledUp(hours) && isMinutesFillUpped(minutes)) {
            editText.setSelection(editText.text.length)
        } else if (isHoursChange(hours, currentCursorPosition) && !isHoursFilledUp(hours)) {
            //   editText.setSelection(hours.toString().length)
        } else if (isMinutesChange(text, minutes, currentCursorPosition) && !isMinutesFillUpped(minutes) && isMinutesEmpty(minutes)) {
            editText.setSelection(currentCursorPosition - 1)
        }
    }

    private fun isHoursFilledUp(hours: Int) = hours != -1 && hours.toString().length == MAX_HOURS_LENGTH

    private fun isMinutesFillUpped(minutes: Int) = minutes != -1 && minutes.toString().length == MAX_MINUTES_LENGTH

    private fun isHoursChange(hours: Int, currentCursorPosition: Int) = currentCursorPosition <= hours.toString().length || (hours.toString().length < MAX_HOURS_LENGTH && currentCursorPosition < MAX_HOURS_LENGTH)

    private fun isMinutesChange(text: String, minutes: Int, currentCursorPosition: Int) = !isMinutesEmpty(minutes) && currentCursorPosition >= text.length - minutes.toString().length

    private fun isMinutesEmpty(minutes: Int) = minutes == -1
}