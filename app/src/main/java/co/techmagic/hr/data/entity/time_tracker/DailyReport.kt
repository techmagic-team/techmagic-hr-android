package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DailyReport(
        @SerializedName("hours") val hours: Int,
        @SerializedName("_task") val task: TaskName,
        @SerializedName("date") val date: Date,
        @SerializedName("_id") val id: String,
        @SerializedName("lockDate") val lockDate: Boolean,
        @SerializedName("note") val note: String
)