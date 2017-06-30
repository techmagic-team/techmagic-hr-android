package co.techmagic.hr.domain.pojo

import co.techmagic.hr.presentation.pojo.WorkingPeriod

/**
 * Created by Roman Ursu on 6/21/17
 */
data class TimeOffRequestByUserAllPeriods(val userId: String, val periods: MutableSet<WorkingPeriod>)