package co.techmagic.hr.data.entity.time_tracker

import co.techmagic.hr.data.entity.HolidayDate
import com.google.gson.annotations.JsonAdapter


@JsonAdapter(UpdateUserReportResponseTypeAdapter::class)
data class UpdateUserReportResponse(
        val firstName: String,
        val lastName: String,
        val submitted: Boolean,
        val reports: UserReport?,
        val weekIds: List<String>,
        val holidays: List<HolidayDate>
)