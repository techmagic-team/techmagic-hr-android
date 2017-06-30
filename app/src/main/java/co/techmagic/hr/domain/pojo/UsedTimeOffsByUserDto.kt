package co.techmagic.hr.domain.pojo

import co.techmagic.hr.common.TimeOffType
import co.techmagic.hr.presentation.pojo.WorkingPeriod
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Roman Ursu on 6/20/17
 */
class UsedTimeOffsByUserDto {
    val timeOffMaps: MutableMap<WorkingPeriod, MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>>>
            = Collections.synchronizedMap(HashMap<WorkingPeriod, MutableMap<TimeOffType, MutableList<RequestedTimeOffDto>>>())

}