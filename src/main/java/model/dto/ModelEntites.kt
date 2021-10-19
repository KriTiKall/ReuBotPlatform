package model.dto

interface LessonEntity {
    fun isEmpty() = true
}

class EmptyLesson(): LessonEntity {

    override fun toString() = "EmptyLesson()"
}

data class Lesson(
    val name: String,
    val teacherName: String,
    val information: String,
): LessonEntity {

    override fun isEmpty() = false
}

data class Schedule(
    var groupName: String,
    var date: String,
    var lessons: Array<LessonEntity>
) {
    init {
        if (lessons.size != 8)
            throw RuntimeException("The array of lesson have not 8 elements")
    }

    constructor() : this("", "", emptyArray())
}