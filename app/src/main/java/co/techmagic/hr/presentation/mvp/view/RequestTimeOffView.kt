package co.techmagic.hr.presentation.mvp.view

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.domain.pojo.RequestedTimeOffDto
import co.techmagic.hr.domain.pojo.UsedTimeOffsByUserDto
import co.techmagic.hr.presentation.pojo.PeriodPair
import java.util.*

/**
 * Created by Roman Ursu on 6/6/17
 */
interface RequestTimeOffView : View {
    fun showDatePicker(from: Calendar, to: Calendar, isDateFromPicker: Boolean)
    fun showTimeOffsDialog()
    fun selectTimeOff(timeOffType: TimeOffType)
    fun showTimeOffsData(daysAmount: Int?)
    fun showUserPeriods(userPeriods: List<PeriodPair>)
    fun showTimeOffsDataError()
    fun showInvalidInputData()
    fun showRequestTimeOffError()
    fun showRequestTimeOffSuccess()
    fun showRequestedTimeOffs(timeOffs: MutableList<RequestedTimeOffDto>)
    fun showErrorLoadingRequestedTimeOffs()
    fun showErrorDeletingRequestedTimeOff()
    fun showUserProfileError()
}