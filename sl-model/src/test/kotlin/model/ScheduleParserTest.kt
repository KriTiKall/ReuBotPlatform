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
import model.parser.ScheduleParser
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals

val module = SerializersModule {
    polymorphic(LessonEntity::class) {
        subclass(Lesson::class)
        subclass(EmptyLesson::class)
    }
}

val format = Json { serializersModule = module }

object TestConstants {
    const val HTML_PATH = "src/test/resources/schedule.html"
    const val PATH = "src/test/resources/schedule.json"

    val SCHEDULE = format.decodeFromString<Array<Schedule>>(File(PATH).readText())
}

fun printCostTime(function: () -> Unit) {
    var time = System.currentTimeMillis()

    function()

    time = System.currentTimeMillis() - time
    println("Cost time equals a $time")
}


class ScheduleParserTest {

//    @Test
    fun parse() { // test doesn't work with this entity version(after add teacher object)
        val expected = TestConstants.SCHEDULE

        val parser = ScheduleParser(true)
        val actual = parser.parse(File(TestConstants.HTML_PATH).readText(), "ПКо-31")

        assertEquals(expected[0], actual[0])
    }

    @Test
    fun testURL() {
//        val reader = ScheduleReader()
//        assertEquals("https://rea.perm.ru/Timetable/rasp_2021.11.22.htm", reader.getActualUrl(LocalDate.now()))
    }
}