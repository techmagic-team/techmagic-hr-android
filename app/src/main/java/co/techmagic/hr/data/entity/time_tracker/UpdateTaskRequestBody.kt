package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName

data class UpdateTaskRequestBody(
        @SerializedName("date") val date: String,
        @SerializedName("firstDayOfWeek") val firstDayOfWeek: String,
        @SerializedName("hours") val minutes: Int,
        @SerializedName("note") val note: String,
        @SerializedName("_project") val projectId: String,
        @SerializedName("_task") val taskId: String,
        @SerializedName("_user") val userId: String
)