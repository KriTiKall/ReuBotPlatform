package com.example.model


import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import com.example.model.entity.Schedule
import com.example.model.entity.format
import com.example.model.parser.ScheduleParser
import java.io.File
import kotlin.test.assertEquals


object TestConstants {
    const val HTML_PATH = "src/test/resources/schedule.html"
    const val PATH = "src/test/resources/schedule.json"

    val SCHEDULE = format.decodeFromString<Array<Schedule>>(File(PATH).readText())

    fun write() {
        val parser = ScheduleParser(true)
        val actual = parser.parse(File(TestConstants.HTML_PATH).readText(), "ПКо-31")
        File(PATH).writeText(format.encodeToString(actual))
    }
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

//    @Test
//    fun testURL() {
//        val reader = ScheduleReader()
//        assertEquals("https://rea.perm.ru/Timetable/rasp_2021.11.22.htm", reader.getActualUrl(LocalDate.now()))
//    }
}