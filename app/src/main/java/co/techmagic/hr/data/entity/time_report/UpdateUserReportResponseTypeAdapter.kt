package co.techmagic.hr.data.entity.time_report

import co.techmagic.hr.data.entity.HolidayDate
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter


class UpdateUserReportResponseTypeAdapter : TypeAdapter<UpdateUserReportResponse>() {
    override fun write(out: JsonWriter?, value: UpdateUserReportResponse?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(reader: JsonReader?): UpdateUserReportResponse {
        var report: UserReport? = null
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
                            "reports" -> {
                                report = Gson().fromJson<UserReport>(reader, UserReport::class.java)
                            }
                            "holidays" -> {
                                // FIXME: returns list of LinkedTreeMap items instead of UserReport
                                // See https://github.com/google/gson/issues/1107
                                // holidays = readList(reader)
                                val list: List<LinkedTreeMap<String, Any>> = readList(reader)
                                val mappedList = list.map {
                                    HolidayDate(
                                            it["_id"] as String,
                                            it["name"] as String,
                                            it["date"] as String
                                    )
                                }
                                holidays = mappedList
                            }
                            "weeksId" -> weeksId = readList(reader)
                        }
                    }
                    JsonToken.BOOLEAN -> reader.nextBoolean()
                    JsonToken.NUMBER -> reader.nextLong()
                    JsonToken.STRING -> reader.nextString()
                    JsonToken.NULL -> reader.nextNull()
                }
            }

            reader.close()
        }

        return UpdateUserReportResponse(
                first ?: "",
                last ?: "",
                submitted ?: false,
                report,
                weeksId ?: emptyList(),
                holidays ?: emptyList()
        )
    }

    private inline fun <reified T> readList(reader: JsonReader): List<T> {
        return Gson().fromJson<Array<T>>(reader, Array<T>::class.java)?.toList() ?: emptyList()
    }
}