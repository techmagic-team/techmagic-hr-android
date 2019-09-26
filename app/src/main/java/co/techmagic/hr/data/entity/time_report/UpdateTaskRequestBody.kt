package co.techmagic.hr.data.entity.time_report

import com.google.gson.annotations.SerializedName

data class UpdateTaskRequestBody(@SerializedName("_user") var userId: String?,
                                 @SerializedName("date") var date: String?,
                                 @SerializedName("firstDayOfWeek") var firstDayOfWeek: String?,
                                 @SerializedName("hours") var minutes: Int?
) {
    @SerializedName("note")
    var note: String? = null
    @SerializedName("rate")
    var rate: Int? = null
    @SerializedName("_project")
    var projectId: String? = null
    @SerializedName("_task")
    var taskId: String? = null

    constructor(date: String?, firstDayOfWeek: String?, minutes: Int?, note: String?, rate: Int?,
                projectId: String?, taskId: String?, userId: String?) : this(userId, date, firstDayOfWeek, minutes) {
        this.note = note
        this.rate = rate
        this.projectId = projectId
        this.taskId = taskId
    }
}