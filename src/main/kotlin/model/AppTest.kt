package model

import model.parser.ScheduleParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.stream.Collectors

class ScheduleReader() {
    private val executor: Executor

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
        val format = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val now = format.format(LocalDateTime.now())
        val parser = ScheduleParser()
//        val url = getURL(now)
        val url = "https://rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-41"

        val schedules = parser.parse(readPage(url), now)

        for (v in schedules)
        println(v)
    }

    private fun readPage(url: String): String {
        var out = ""
        val connection = URL(url).openConnection()

        connection.getInputStream().let {
            BufferedReader(InputStreamReader(it))
        }.use {
            out = it.lines().collect(Collectors.joining("\n"));
        }

        return out
    }

    private fun getURL(date: String) = "https://rea.perm.ru/Timetable/rasp_2021.10.15.htm?page_id=1036&id=Timetable/rasp_$date"
}

