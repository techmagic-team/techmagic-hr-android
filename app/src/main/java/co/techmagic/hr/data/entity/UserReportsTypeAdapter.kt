package co.techmagic.hr.data.entity

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class UserReportsTypeAdapter : TypeAdapter<UserReports>() {
    override fun write(out: JsonWriter?, value: UserReports?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(`in`: JsonReader?): UserReports {
        var reports: List<UserReport>? = null
        if (`in` != null) {
            `in`.beginObject()
            `in`.nextName() //user id
            `in`.beginObject()
            reports = Gson().fromJson<Array<UserReport>>(`in`, Array<UserReport>::class.java)?.toList()
        }
        return UserReports(reports ?: emptyList())
    }
}