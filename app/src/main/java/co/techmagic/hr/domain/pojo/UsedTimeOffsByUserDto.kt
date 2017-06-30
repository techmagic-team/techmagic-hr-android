package co.techmagic.hr.domain.pojo

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.presentation.pojo.PeriodPair
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Roman Ursu on 6/20/17
 */
class UsedTimeOffsByUserDto {
    val timeOffMaps: MutableMap<PeriodPair, MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>>>
            = Collections.synchronizedMap(HashMap<PeriodPair, MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>>>())

}