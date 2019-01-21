package co.techmagic.hr.data.entity

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(ProjectsTypeAdapter::class)
data class Projects(
        val list: List<Project>
)

data class Project(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("_client") val client: Client,
        @SerializedName("_delivery") val delivery: Delivery,
        @SerializedName("note") val note: String,
        @SerializedName("_tasks") val tasks: List<TaskEntry>,
        val internal: Boolean,
        val projectType: String
)

data class Client(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)

data class Delivery(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String
)
