package model.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.parser.ScheduleParser
import java.io.File

val module = SerializersModule {
    polymorphic(LessonEntity::class) {
        subclass(EmptyLesson::class)
        subclass(Lesson::class)
    }
    polymorphic(Indivisible::class) {
        subclass(SingleLesson::class)
        subclass(PairLesson::class)
    }
}

val format = Json { serializersModule = module }

object TestConstants {
    const val HTML_PATH = "src/test/resources/schedule.html"
    const val PATH = "src/test/resources/schedule.json"
    const val PATH1 = "/home/yrik/Рабочий стол/dm/ya/exam/src/main/resources/sch.json"

    val SCHEDULE = format.decodeFromString<Array<Schedule>>(File(PATH).readText())

    fun write() {
        val parsesr = ScheduleParser(true)
        val actuasl = parsesr.parse(File(HTML_PATH).readText(), "ПКо-31")
        File(PATH).writeText(format.encodeToString(actuasl))
    }
}

interface LessonEntity {
    fun isEmpty() = true
}

interface Indivisible {
    fun isIndivisible() = true
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
data class SingleLesson(

    val lesson: LessonEntity
) : Indivisible

@Serializable
data class PairLesson(

    val pair: Pair<LessonEntity, LessonEntity>
) : Indivisible {

    override fun isIndivisible() = false
}

@Serializable
data class Schedule(
    var groupName: String,
    var date: String,
    var lessons: Array<Indivisible>
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