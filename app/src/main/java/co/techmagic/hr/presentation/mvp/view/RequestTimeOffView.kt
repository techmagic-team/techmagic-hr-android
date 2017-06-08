package co.techmagic.hr.presentation.mvp.view

import co.techmagic.hr.common.TimeOffType
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
interface RequestTimeOffView : View {
    fun showDatePicker(from: Calendar, to: Calendar, isDateFromPicker: Boolean)
    fun showTimeOffsDialog()
    fun selectTimeOff(timeOffType: TimeOffType)
}