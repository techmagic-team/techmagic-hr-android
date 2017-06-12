package co.techmagic.hr.data.request

import co.techmagic.hr.data.entity.DateFrom
import co.techmagic.hr.data.entity.DateTo

/**
 * Created by Roman Ursu on 6/12/17
 */
data class RemainedTimeOffRequest(val userId: String, val dateFrom: DateFrom, val dateTo: DateTo)