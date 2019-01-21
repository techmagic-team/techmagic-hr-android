package co.techmagic.hr.data.entity

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class TasksTypeAdapter : TypeAdapter<Tasks>() {
    override fun write(out: JsonWriter?, value: Tasks?) {
        TODO("Not implemented yet. Implement if needed")
    }

    override fun read(`in`: JsonReader?): Tasks {
        var tasks: List<TaskEntry>? = null
        if (`in` != null) {
            tasks = Gson().fromJson<Array<TaskEntry>>(`in`, Array<TaskEntry>::class.java)?.toList()
        }
        return Tasks(tasks ?: emptyList())
    }
}