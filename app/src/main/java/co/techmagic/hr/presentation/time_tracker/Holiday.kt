package co.techmagic.hr.presentation.time_tracker

enum class Holiday(val id: String) {
    CHRISTMAS("Christmas"),
    CONSTITUTION_DAY("Constitution Day of Ukraine"),
    DEFENDERS_DAY("Defender Day Ukraine"),
    EASTER("Easter"),
    INDEPENDENCE_DAY("Ukraine Independence Day"),
    NEW_YEAR("New Year"),
    PENTECOST("Holy Trinity Day"),
    WOMEN_DAY("International Women's Day"),
    OTHER("other");

    companion object {
        fun fromString(name: String): Holiday {
            return try {
                Holiday.values().first { it.id == name }
            } catch (e: Exception) {
                OTHER
            }
        }
    }
}