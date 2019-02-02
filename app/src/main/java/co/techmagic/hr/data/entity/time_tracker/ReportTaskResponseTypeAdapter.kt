package co.techmagic.hr.data.entity.time_tracker

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ReportTaskResponseTypeAdapter : TypeAdapter<ReportTaskResponse>() {
    override fun write(out: JsonWriter?, value: ReportTaskResponse?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(reader: JsonReader?): ReportTaskResponse {
        if (reader != null) {
            reader.beginObject()
            reader.nextName()

            reader.beginObject()
            reader.nextName()

            val gson = Gson()
            val report = gson.fromJson<UserReport>(reader, UserReport::class.java)
            reader.nextName()
            val weekIds = gson.fromJson<Array<String>>(reader, Array<String>::class.java).toList()
            return ReportTaskResponse(report, weekIds)
        }
        throw IllegalArgumentException("Json reader is null")
    }
}