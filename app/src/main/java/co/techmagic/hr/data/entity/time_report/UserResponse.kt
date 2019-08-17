package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("_id") val id: String,
        val isDelivery: Boolean,
        val isTeamLead: Boolean,
        @SerializedName("tr_isAdmin") val isAdmin: Boolean,
        val role: Int
)