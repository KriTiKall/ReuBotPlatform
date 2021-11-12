package model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.entity.EmptyLesson
import model.entity.Lesson
import model.entity.LessonEntity
import model.entity.Schedule
import org.junit.jupiter.api.Test
import java.io.File

val module = SerializersModule {
    polymorphic(LessonEntity::class) {
        subclass(Lesson::class)
        subclass(EmptyLesson::class)
    }
}

val format = Json { serializersModule = module }

object TestConstants {
    const val HTML_PATH = "src/test/resources/schedule.html"
    private const val PATH = "src/test/resources/schedule.json"
    val SCHEDULE = format.decodeFromString<Array<Schedule>>(File(PATH).readText())
}


internal class ScheduleParserTest {

    @Test
    fun parse() {

    }
}

fun main() {
    TestConstants.SCHEDULE.forEach(::println)
}
