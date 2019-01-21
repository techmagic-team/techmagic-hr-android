package co.techmagic.hr.data.entity

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName


@JsonAdapter(TasksTypeAdapter::class)
data class Tasks(
        val tasks: List<TaskEntry>
)

data class TaskEntry(
        @SerializedName("_id") val id: String,
        @SerializedName("hours") val name: Int,
        @SerializedName("_task") val task: Task
)

data class Task(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)