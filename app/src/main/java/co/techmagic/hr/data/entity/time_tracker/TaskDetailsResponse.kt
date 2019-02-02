package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskDetailsResponse(
        @SerializedName("project") val project: Project,
        @SerializedName("client") val client: ClientName,
        @SerializedName("status") val status: String,
        @SerializedName("firstDayOfWeek") val firstDayOfWeek: Date,
        @SerializedName("dailyReports") val dailyReports: List<DailyReport>
)