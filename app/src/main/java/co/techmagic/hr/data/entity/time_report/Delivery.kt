package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName

data class DeliveryManager(
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("_id:") val id: String
)

data class Delivery(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("_manager") val manager: DeliveryManager
)