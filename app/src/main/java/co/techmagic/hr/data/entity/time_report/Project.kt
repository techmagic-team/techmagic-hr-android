package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName

data class Project(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)