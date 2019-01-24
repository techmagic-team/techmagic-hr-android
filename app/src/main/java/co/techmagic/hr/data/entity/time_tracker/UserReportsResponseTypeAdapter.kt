package co.techmagic.hr.data.entity.time_tracker

import co.techmagic.hr.data.entity.HolidayDate
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter


class UserReportsResponseTypeAdapter : TypeAdapter<UserReportsResponse>() {
    override fun write(out: JsonWriter?, value: UserReportsResponse?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(reader: JsonReader?): UserReportsResponse {
        var reports: List<UserReport>? = null
        var weeksId: List<String>? = null
        var holidays: List<HolidayDate>? = null
        var first: String? = null
        var last: String? = null
        var submitted: Boolean? = null

        if (reader != null) {
            while (JsonToken.END_DOCUMENT != reader.peek()) {
                val token = reader.peek()
                when (token) {
                    JsonToken.BEGIN_OBJECT -> reader.beginObject()
                    JsonToken.BEGIN_ARRAY -> reader.beginArray()
                    JsonToken.END_ARRAY -> reader.endArray()
                    JsonToken.END_OBJECT -> reader.endObject()
                    JsonToken.NAME -> {
                        val name = reader.nextName()
                        when (name) {
                            "firstName" -> first = reader.nextString()
                            "lastName" -> last = reader.nextString()
                            "submitted" -> submitted = reader.nextBoolean()
                            "reports" -> reports = readList(reader)
                            "holidays" -> holidays = readList(reader)
                            "weeksId" -> weeksId = readList(reader)
                        }
                    }
                }
            }

            reader.close()
        }

        return UserReportsResponse(
                first ?: "",
                last ?: "",
                submitted ?: false,
                reports ?: emptyList(),
                weeksId ?: emptyList(),
                holidays ?: emptyList()
        )
    }

    private inline fun <reified T> readList(reader: JsonReader): List<T> {
        return Gson().fromJson<Array<T>>(reader, Array<T>::class.java)?.toList() ?: emptyList()
    }
}