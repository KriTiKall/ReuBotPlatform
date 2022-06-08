package com.example.model

import com.example.model.entity.Schedule
import com.example.model.parser.Parser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

interface IScheduleService {

    fun saveOrUpdate(schedule: Schedule): Boolean
}


class ScheduleReader(
    private val parser: Parser,
    private val service: IScheduleService
) : Runnable {

    private val formatterInURL = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")

    private var readed = false

    override fun run() {
        try {
            val currentTime = LocalTime.now()
            var array: Array<Schedule>

            // every day in 5 am parsing timetable on week forward
            if (currentTime.hour == 5 && !readed) {
                var date = LocalDate.now()
                date += 2
                for (i in 2..8) {
                    array = parseSchedule(date)
                    array.forEach(service::saveOrUpdate)
                    date++
                }
                readed = true
            }

            // to store today's and tomorrow's schedule
            if (currentTime.minute % 2 == 0) {
                var date = LocalDate.now()
                array = parseSchedule(date)
                array.forEach(service::saveOrUpdate)

                date++
                array = parseSchedule(date)
                array.forEach(service::saveOrUpdate)
            }

            if (currentTime.hour == 6 && readed) {
                readed = false
            }
        } catch (e: SQLException) {
            println(e.stackTraceToString())
        }
    }

    fun parseSchedule(date: LocalDate): Array<Schedule> { // todo make private
        val html = getHtml(date)

        return parser.parse(html, getFormatDate(date))
    }

    private fun getHtml(date: LocalDate): String {
        val url = "https://rea.perm.ru/Timetable/rasp_${getFormatDate(date, formatterInURL)}.htm"
        return readPage(url)
    }

    private fun getFormatDate(date: LocalDate, format: DateTimeFormatter = formatterInSchedule): String {
        return date.format(format)
    }

    private fun readPage(url: String): String {
        var out: String
        val connection = URL(url).openConnection()

        BufferedReader(InputStreamReader(connection.getInputStream())).use {
            out = it.lines().collect(Collectors.joining("\n"));
        }

        return out
    }
}

private val increment = Calendar.getInstance()

private operator fun LocalDate.inc(): LocalDate {
    increment.time = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    increment.add(Calendar.DATE, 1)
    return increment.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

private operator fun LocalDate.plus(i: Int): LocalDate {
    increment.time = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    increment.add(Calendar.DATE, i)
    return increment.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

class MockScheduleService : IScheduleService {
    override fun saveOrUpdate(schedule: Schedule): Boolean {
        println(schedule)
        return true
    }
}