package model.parser

import model.entity.EmptyLesson
import model.entity.Lesson
import model.entity.LessonEntity
import model.entity.Schedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
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
                var infos = selectInfos(it)
                val lessons = multiLayerToOneLayer(it)
                composeSchedule(lessons, title, infos)
            }
    }

    private fun multiLayerToOneLayer(trs: Elements): Array<Array<LessonEntity>> {
        val lessons = Array(8) { mutableListOf<LessonEntity>() }

        removeUnnecessary(trs).forEachIndexed { i, tr ->
            lessons[i % 8].addAll(trToLessons(tr))
        }

        return rotate90(lessons)
    }

    private fun removeUnnecessary(trs: Elements) =
        trs.filterIndexed { ind, _ ->
            ind % 9 != 0
        }

    private fun trToLessons(tr: Element): MutableList<LessonEntity> { //todo rewrite using pairLesson
        val list = mutableListOf<LessonEntity>()

        val tds = tr.select("td").apply {
            removeAt(0)
        }
        var size = tds.size - 1

        for (i in 0..size) {
            val td = tds.get(i)
            val lesson = tdToLesson(td)

            if (td.toString().contains("colspan=\"3\"")) { //todo use field attributes
                val half = tdToLesson(tds.get(i + 1)) // todo edit all lesson field
                var firstAud: String
                var secondAud: String
                var rm = 1

                if (lesson is Lesson) {
                    firstAud = lesson.auditorium

                    if (half is Lesson)
                        secondAud = half.auditorium
                    else
                        secondAud = half.toString()

                    lesson.auditorium = "$firstAud/$secondAud" // "305 он-лайн д/307 он-лайн д"
                } else {
                    firstAud = lesson.toString()

                    if (half is Lesson) {
                        secondAud = half.auditorium
                        half.auditorium = "$firstAud/$secondAud"
                        rm = 0
                    } else
                        secondAud = half.toString()
                }

                tds.removeAt(i + rm)
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

        var opInfo = splits.get(1)

        val teacherName =
            if (opInfo.contains(", ")) {
                opInfo.split(", ")[1]
            } else {
                opInfo
            }

        opInfo = splits.get(2)

        val (type, auditorium) =
            if (opInfo.contains(", ауд.")) {
                val tmp = opInfo.split(", ауд.").map { it.trim() }
                Pair(tmp[0], tmp[1])
            } else {
                Pair(opInfo, "")
            }

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

    private fun selectInfos(tr: Elements): Array<String> =
        tr.filterIndexed { ind, _ ->
            ind % 9 == 0
        }.flatMap {
            it.select("td").apply {
                removeAt(0)
            }
        }.map(Element::text).toTypedArray()


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