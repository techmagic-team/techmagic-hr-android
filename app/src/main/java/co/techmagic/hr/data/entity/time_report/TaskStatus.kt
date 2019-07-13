package co.techmagic.hr.data.entity.time_report

enum class TaskStatus {
    PENDING,
    APPROVED;

    companion object {
        fun fromString(status: String?): TaskStatus {
            return when(status) {
                "APPROVED" -> APPROVED
                else -> PENDING
            }
        }
    }
}