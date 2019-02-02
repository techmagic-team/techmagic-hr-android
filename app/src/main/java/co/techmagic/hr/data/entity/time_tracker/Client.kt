package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName

data class Client(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)