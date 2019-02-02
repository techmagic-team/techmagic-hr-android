package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.annotations.SerializedName


data class TaskResponse(
        @SerializedName("_id") val id: String,
        @SerializedName("hours") val name: Int,
        @SerializedName("_task") val task: Task
)