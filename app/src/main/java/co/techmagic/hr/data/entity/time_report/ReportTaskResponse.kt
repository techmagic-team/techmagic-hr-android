package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(ReportTaskResponseTypeAdapter::class)
data class ReportTaskResponse(
        @SerializedName("reports") val report: UserReport,
        @SerializedName("weeksId") val weeksId: List<String>
)