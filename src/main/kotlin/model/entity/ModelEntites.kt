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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

@Serializable
data class Lesson(
    val name: String,
    val teacherName: String,
    @SerialName("LessonType")
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (groupName != other.groupName) return false
        if (date != other.date) return false
        if (!lessons.contentEquals(other.lessons)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupName.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + lessons.contentHashCode()
        return result
    }
}