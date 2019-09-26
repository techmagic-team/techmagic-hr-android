package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserReport constructor(
        @SerializedName("_id") val id: String,
        @SerializedName("_task") val task: ReportName,
        @SerializedName("hours") var minutes: Int,
        val date: Date,
        val lockDate: Boolean, //'true' in vacation reports
        val note: String,
        val status: String,
        val client: String,
        val project: String,
        val weekReportId: String) {

    val isApproved: Boolean
        get() = TaskStatus.fromString(status) == TaskStatus.APPROVED
}