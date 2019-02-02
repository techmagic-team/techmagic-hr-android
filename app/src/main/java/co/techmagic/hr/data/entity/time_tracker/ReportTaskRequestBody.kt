package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName

data class ReportTaskRequestBody(
        @SerializedName("date") val date: String,
        @SerializedName("firstDayOfWeek") val firstDayOfWeek: String,
        @SerializedName("hours") val hours: Int,
        @SerializedName("note") val note: String,
        @SerializedName("rate") val rate: Int,
        @SerializedName("_client") val clientId: String,
        @SerializedName("_company") val companyId: String,
        @SerializedName("_project") val projectId: String,
        @SerializedName("_task") val taskId: String,
        @SerializedName("_user") val userId: String
)