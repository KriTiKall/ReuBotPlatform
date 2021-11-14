package model.parser

import model.entity.EmptyLesson
import model.entity.Lesson
import model.entity.LessonEntity
import model.entity.Schedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.io.IOException

interface Parser {

    @Throws(IOException::class)
    fun parse(html: String, name: String): Array<Schedule>
}

class ScheduleParser(val withName: Boolean = false) : Parser {

    override fun parse(html: String, title: String): Array<Schedule> {
        return Jsoup.parse(html)
            .body()
            .select("table")
            .first()!!
            .select("tr[class=\"fon\"], tr[valign=\"top\"]")
            .let {
                val row = it.get(0)
                val lessons = multiLayerToOneLayer(it)
                var infos = selectInfos(row)
                composeSchedule(lessons, title, infos)
            }
    }

    private fun multiLayerToOneLayer(trs: Elements): Array<Array<LessonEntity>> {
        val lessons = Array(8) { mutableListOf<LessonEntity>() }
        trs.removeAt(0)
        // TODO: 18.10.2021 rewrite using reduce where  acc = elements

        trs.forEachIndexed { i, tr ->
            lessons[i % 8].addAll(trToLessons(tr))
        }
        return rotate90(lessons)
    }

    private fun trToLessons(tr: Element): MutableList<LessonEntity> {
        val list = mutableListOf<LessonEntity>()

        val tds = tr.select("td").apply {
            removeAt(0)
        }
        var size = tds.size - 1

        for (i in 0..size) {
            val td = tds.get(i)
            val lesson = tdToLesson(td)

            if (td.toString().contains("colspan=\"3\"")) { //todo use field attributes
                val auditorium = (tdToLesson(tds.get(i + 1)) as Lesson).auditorium
                (lesson as Lesson).auditorium += "/$auditorium" // todo edit
                tds.removeAt(i + 1)
                size = tds.size - 1
            }
            list.add(lesson)
        }
        return list
    }

    private fun tdToLesson(td: Element): LessonEntity =
        td.html()
            .split("<br>")
            .takeIf { it.size == 3 }
            ?.let(::composeLesson)
            ?: EmptyLesson()

    private fun composeLesson(splits: List<String>): Lesson {
        val name = splits.get(0)
            .substring(3, splits.get(0).length - 4)
            .trim { it <= ' ' }

        val teacherName = splits.get(1)

        val opInfo = splits.get(2)
        val type = opInfo.substring(0, opInfo.indexOf(','))
        val auditorium = opInfo.substring(opInfo.indexOf("ауд. ") + 5)

        return Lesson(
            name,
            teacherName,
            type,
            auditorium
        )
    }

    private fun rotate90(lessons: Array<MutableList<LessonEntity>>): Array<Array<LessonEntity>> {
        val result = Array(lessons[0].size) { Array<LessonEntity>(lessons.size) { EmptyLesson() } }
        var lesson: LessonEntity

        for (i in result.indices) {
            for (j in result[0].indices) {
                lesson = lessons[j].get(i)
                if (lesson !is EmptyLesson)
                    result[i][j] = lesson
            }

        }
        return result
    }

    private fun selectInfos(tr: Element): Array<String> =
        tr.select("td")
            .apply {
                removeAt(0)
            }
            .map(Element::text)
            .toTypedArray()

    // INFO length of array is number of day in schedule.
    private fun composeSchedule(
        lessons: Array<Array<LessonEntity>>,
        data: String,
        infos: Array<String>
    ): Array<Schedule> {
        var name: String
        var date: String

        return infos.mapIndexed { ind, el ->
            date = data
            name = el
            if (withName) {
                date = el
                name = data
            }

            Schedule(
                name,
                date,
                lessons[ind]
            )
        }.toTypedArray()
    }

}