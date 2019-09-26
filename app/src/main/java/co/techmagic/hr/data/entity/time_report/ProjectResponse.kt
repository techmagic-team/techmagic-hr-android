package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName


data class ProjectResponse(
        @SerializedName("_id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("_client") val client: Client?,
        @SerializedName("_delivery") val delivery: Delivery?,
        @SerializedName("note") val note: String?,
        @SerializedName("_tasks") val tasks: List<TaskResponse>?,
        val internal: Boolean?,
        val projectType: String?
) {
    constructor(id: String, name: String, client: Client?) : this(id, name, client, null, null, null, null, null)
}
