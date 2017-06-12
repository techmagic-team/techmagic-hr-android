package co.techmagic.hr.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Roman Ursu on 6/12/17
 */
data class TimeOffAmount(
        @SerializedName("availableDays")
        var availableDays: Int)