package co.techmagic.hr.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Roman Ursu on 6/14/17
 */
data class DatePeriod(
        @SerializedName("dateFrom") val dateFrom: String,
        @SerializedName("dateTo") val dateTo: String)