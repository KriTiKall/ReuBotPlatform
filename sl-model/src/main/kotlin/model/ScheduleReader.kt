package model

import data.dao.IScheduleDao
import model.entity.Schedule
import model.parser.Parser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

class ScheduleReader(
    private val parser: Parser,
    private val dao: IScheduleDao
) : Runnable {

    private val formatterInURL = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")

    //        Calendar cal = Calendar.getInstance();
    //        Date input = cal.getTime();
    //        LocalDate la = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    //    val c = Calendar.getInstance();
    //    c.setTime(sdf.parse(str));
    //    c.add(Calendar.DATE, 1);

    override fun run() {
        var currentTime = LocalTime.now()
        var array: Array<Schedule>
        var readed = false



        // every day in 5 am parsing timetable on week forward
        if (currentTime.hour == 5 && !readed) {
            var date = LocalDate.now()
            date += 2
            for (i in 2..8) {
                array = parseSchedule(date)
                array.forEach(this::read)
                date++
            }
        }



    }

    private fun read(schedule: Schedule) {
        val hash = dao.getHash(schedule.groupName, schedule.date)
        if (hash == null)
            dao.save(schedule)
        else {
            if (hash != schedule.hashCode()) {
                dao.update(schedule)
            }
        }
    }

    private fun parseSchedule(date: LocalDate): Array<Schedule> {
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
        var out = ""
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


//private operator fun LocalDate.plusAssign(add: Int) {
//    increment.time = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
//    increment.add(Calendar.DATE, add)
//    this = increment.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//}