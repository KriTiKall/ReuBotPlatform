package model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface LessonEntity {
    fun isEmpty() = true
}

@Serializable
@SerialName("Empty")
class EmptyLesson(): LessonEntity {

    override fun toString() = "Empty"
}

@Serializable
data class Lesson(
    val name: String,
    val teacherName: String,
    val type: String = "",
    var auditorium: String = ""
): LessonEntity {

    override fun isEmpty() = false
}

@Serializable
data class Schedule(
    var groupName: String,
    var date: String,
    var lessons: Array<LessonEntity>
) {
    init {
        if (lessons.size != 8)
            throw RuntimeException("The array of lesson have not 8 elements(size=${lessons.size})")
    }

    constructor() : this("", "", emptyArray())
}