package co.techmagic.hr.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Roman Ursu on 6/29/17
 */
class HolidayDate(@SerializedName("_id") val id: String,
                  @SerializedName("name") val name: String,
                  @SerializedName("date") val date: String)