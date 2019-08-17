package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DeleteTaskRequestBody(
        @SerializedName("date") val date: Date,
        @SerializedName("_user") val userId: String,
        @SerializedName("_project") val projectId: String
)