package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName

data class Delivery(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)