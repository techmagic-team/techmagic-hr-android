package co.techmagic.hr.presentation.util

import android.text.Editable
import android.util.Log
import android.widget.EditText
import java.util.regex.Pattern

class TimeInputTextWatcher(val editText: EditText) : SimpleTextWatcher() {

    private var isInnerChange = false

    companion object {
        const val INPUT_PATTERN = "\\d*:\\d*"
        const val HOURS_PATTERN = "\\d{1,2}:"
        const val MINUTES_PATTERN = ":\\d{1,2}"

        const val MAX_HOURS = 24
        const val MAX_MINUTES = 59

        const val MAX_HOURS_LENGTH = 2
        const val MAX_MINUTES_LENGTH = 2

        const val MAX_HOURS_CURSOR_POSITION = 2
        const val MAX_MINUTES_CURSOR_POSITION = 5
    }

    override fun afterTextChanged(s: Editable?) {
        s ?: return
        if (isInnerChange) return

        try {
            val text = s.toString()
            var hours = getHours(text)
            var minutes = getMinutes(text)

            if (hours > MAX_HOURS) {
                hours = MAX_HOURS
            }

            if (minutes > MAX_MINUTES) {
                minutes = MAX_MINUTES
            }

            isInnerChange = true
            s.replace(0, s.length, getValidTime(hours, minutes))
            moveCursor(hours, minutes, editText.selectionEnd)
            isInnerChange = false
        } catch (ex: Exception) {
            Log.d("TEST_EX", ex.message)
        }

    }

    private fun getHours(text: String): Int {
        val hoursMatcher = Pattern
                .compile(HOURS_PATTERN)
                .matcher(text)
        return if (hoursMatcher.find())
            hoursMatcher.group().dropLast(1).toInt()
        else
            0
    }

    private fun getMinutes(text: String): Int {
        val minutesMatcher = Pattern
                .compile(MINUTES_PATTERN)
                .matcher(text)

        //todo fix dropLast
        return if (minutesMatcher.find())
            minutesMatcher.group().drop(1).toInt()
        else
            0
    }

    private fun getValidTime(hours: Int, minutes: Int) = String.format("%d:%d", hours, minutes)

    private fun moveCursor(hours: Int, minutes: Int, currentCursorPosition: Int) {
        Log.d("val editText : EditText", "$currentCursorPosition")
        if (hours.toString().length == MAX_HOURS_LENGTH && currentCursorPosition <= MAX_HOURS_CURSOR_POSITION) {
            editText.setSelection(3)
        } else if (editText.text.length == MAX_MINUTES_LENGTH && currentCursorPosition <= MAX_HOURS_CURSOR_POSITION) {
            editText.setSelection(MAX_MINUTES_CURSOR_POSITION)
        }
    }
}