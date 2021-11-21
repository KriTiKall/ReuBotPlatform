package model

import model.entity.Schedule
import model.parser.ScheduleParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.stream.Collectors

class ScheduleReader() {
    private val executor: Executor
    private val formatterInURL = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")

    init {
        executor = Executors.newFixedThreadPool(4)
    }

    fun start() {

        val dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val now = LocalDateTime.now()
        executor.execute {

        }
    }

    fun test() {
        val now = formatterInURL.format(LocalDateTime.now())
        val parser = ScheduleParser()
//        val url = getURL(now)
        val url = "https://rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-41"

        val schedules = parser.parse(readPage(url), now)

        for (v in schedules)
            println(v)
    }

    fun secTest() {
        val html = getHtml(LocalDate.now())
        val parser = ScheduleParser()


        val schedules = parser.parse(html, getFormatDate(LocalDate.now()))

        for (v in schedules)
            println(toStr(v))
    }

    fun getHtml(date: LocalDate): String {
        return readPage(getActualUrl(date))
    }

    private fun readPage(url: String): String {
        var out = ""
        val connection = URL(url).openConnection()

        BufferedReader(InputStreamReader(connection.getInputStream())).use {
            out = it.lines().collect(Collectors.joining("\n"));
        }

        return out
    }

    fun getActualUrl(date: LocalDate): String {
        val str = getFormatDate(date, formatterInURL)
        return "https://rea.perm.ru/Timetable/rasp_$str.htm"
    }

    fun getFormatDate(date: LocalDate, format: DateTimeFormatter = formatterInSchedule): String {
        return date.format(format)
    }
}

fun main () {
    val parser = ScheduleReader()
    parser.secTest()
}

fun toStr(schedule: Schedule) : String {
    val sb = StringBuilder()
    schedule.lessons.forEachIndexed { ind, el ->
        sb.append("\t").append(ind + 1).append(". ").append(el).append("\n")
    }
    return "${schedule.groupName}  ${schedule.date} \n${sb.toString()}"
}