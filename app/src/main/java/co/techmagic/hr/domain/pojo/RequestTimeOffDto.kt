package co.techmagic.hr.domain.pojo

import co.techmagic.hr.common.TimeOffType

/**
 * Created by Roman Ursu on 6/19/17
 */
data class RequestTimeOffDto(val dateFrom: Long, val dateTo: Long, val userId: String, val timeOffType: TimeOffType, val isAccepted: Boolean?)