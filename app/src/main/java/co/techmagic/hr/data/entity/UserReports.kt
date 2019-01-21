package co.techmagic.hr.data.entity

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

@JsonAdapter(UserReportsTypeAdapter::class)
data class UserReports(
//        val firstName: String,
//        val lastName: String,
        val reports: List<UserReport>
)

data class UserReport(
        @SerializedName("_id") val id: String,
        @SerializedName("_task") val task: ReportTask,
        @SerializedName("hours") val minutes: Long,
        val date: Date,
        val lockDate: Boolean,
        val note: String,
        val status: String,
        val client: String,
        val project: String,
        val weekReportId: String
)

data class ReportTask(val name: String)