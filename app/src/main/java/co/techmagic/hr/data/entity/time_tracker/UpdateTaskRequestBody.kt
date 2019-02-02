package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UpdateTaskRequestBody(
        @SerializedName("date") val date: Date,
        @SerializedName("firstDayOfWeek") val firstDayOfWeek: String,
        @SerializedName("hours") val minutes: Int,
        @SerializedName("note") val note: String,
        @SerializedName("rate") val rate: Int,
        @SerializedName("_project") val projectId: String,
        @SerializedName("_task") val taskId: String,
        @SerializedName("_user") val userId: String
)