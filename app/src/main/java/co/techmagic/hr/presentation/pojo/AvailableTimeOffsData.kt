package co.techmagic.hr.presentation.pojo

import co.techmagic.hr.domain.pojo.RemainedTimeOffsAmountDto
import java.util.*

/**
 * Created by Roman Ursu on 6/15/17
 */
class AvailableTimeOffsData {
    var timeOffsMap: MutableMap<PeriodPair, RemainedTimeOffsAmountDto> = Collections.synchronizedMap(HashMap())
        private set
}