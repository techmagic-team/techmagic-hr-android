package co.techmagic.hr.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Roman Ursu on 6/19/17
 */
data class RequestTimeOff(
        @SerializedName("dateFrom") val dateFrom: Long,
        @SerializedName("dateTo") val dateTo: Long,
        @SerializedName("_user") val userId: String,
        @SerializedName("isPaid") val isPaid: Boolean,
        @SerializedName("isAccepted") val isAccepted: Boolean?)