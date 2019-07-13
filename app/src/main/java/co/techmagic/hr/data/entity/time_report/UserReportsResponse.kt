package co.techmagic.hr.data.entity.time_report

import co.techmagic.hr.data.entity.HolidayDate
import com.google.gson.annotations.JsonAdapter


@JsonAdapter(UserReportsResponseTypeAdapter::class)
data class UserReportsResponse(
        val firstName: String,
        val lastName: String,
        val submitted: Boolean,
        val reports: List<UserReport>,
        val weekIds: List<String>,
        val holidays: List<HolidayDate>
)