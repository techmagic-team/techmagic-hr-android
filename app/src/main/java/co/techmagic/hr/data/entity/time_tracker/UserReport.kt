package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserReport(
        @SerializedName("_id") val id: String,
        @SerializedName("_task") val task: ReportName,
        @SerializedName("hours") var minutes: Int,
        val date: Date,
        val lockDate: Boolean,
        val note: String,
        val status: String,
        val client: String,
        val project: String,
        val weekReportId: String
)