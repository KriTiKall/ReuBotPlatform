package data.entites

import kotlinx.serialization.encodeToString
import model.entity.Schedule
import model.entity.format

class ScheduleDbMapper { // todo see how do this better
    lateinit var date: String
    lateinit var name: String
    var hash: Int = 0
    lateinit var lessonJsonb: Array<String>

    fun set(schedule: Schedule): ScheduleDbMapper {
        schedule.let {
            date = it.date
            name = it.groupName
            hash = it.hashCode()
            lessonJsonb = it.lessons.map { l -> format.encodeToString(HashWrapper(l, l.hashCode())) }.toTypedArray()
        }
        return this
    }


    override fun toString(): String {
        return "('$date', '$name', $hash, (ARRAY[${lessonJsonb.joinToString()}])::model.lessons_json)::model.schedule))"
    }
}