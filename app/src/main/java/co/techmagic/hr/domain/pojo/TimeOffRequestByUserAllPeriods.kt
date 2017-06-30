package co.techmagic.hr.domain.pojo

import co.techmagic.hr.presentation.pojo.PeriodPair

/**
 * Created by Roman Ursu on 6/21/17
 */
data class TimeOffRequestByUserAllPeriods(val userId: String, val periods: MutableSet<PeriodPair>)