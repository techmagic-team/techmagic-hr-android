package co.techmagic.hr.data.entity

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class ProjectsTypeAdapter : TypeAdapter<Projects>() {
    override fun write(out: JsonWriter?, value: Projects?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(`in`: JsonReader?): Projects {
        var projects: List<Project>? = null
        if (`in` != null) {
            projects = Gson().fromJson<Array<Project>>(`in`, Array<Project>::class.java)?.toList()
        }
        return Projects(projects ?: emptyList())
    }
}