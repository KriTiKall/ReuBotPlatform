package model.parser

import model.dto.EmptyLesson
import model.dto.Lesson
import model.dto.LessonEntity
import model.dto.Schedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

interface Parser {
    //todo url -> html
    @Throws(IOException::class)
    fun parse(html: String, name: String): Array<Schedule>
}

class ParserWithName : Parser {

    override fun parse(html: String, name: String): Array<Schedule> {
        return Jsoup.parse(html)
            .body()
            .select("table")
            .first()
            .select("tr[class=\"fon\"], tr[valign=\"top\"]")
            .let {
                val trs = multiLayerToOneLayer(it)
                composeSchedule(parseLessons(trs), name, trs)
            }
    }

    private fun multiLayerToOneLayer(trs: Elements): Array<Elements> {
        val elements = Array(9) { Elements() }
        // TODO: 18.10.2021 rewrite using reduce where  acc = elements
        trs.forEachIndexed { i, tr ->
            elements[i % 9].addAll(trToNormalTds(tr))
        }
        return elements
    }

    private fun trToNormalTds(tr: Element): Elements {
        val tds = tr.select("td").apply {
            removeAt(0)
        }

        /*for (i in tds.indices) {
            if (tds.get(i).html().matches(Regex("colspan=\"3\""))) {
                // TODO: 17.10.2021 add a logic for colspan = 3 and colspane = 6
            }
        }*/
        return tds
    }

    private fun parseLessons(trArray: Array<Elements>): Array<Array<LessonEntity>> =
        Array(trArray[0].size) {
            // the matrix width is count of day in schedule. eight is number pair of schedule max
            Array<LessonEntity>(8) { EmptyLesson() }
        }.mapIndexed { i, el ->
            el.mapIndexed { j, ell ->
                elementToLesson(trArray.get(j + 1).get(i))
            }.toTypedArray()
        }.toTypedArray()

    // mapping td element to Discipline object
    private fun elementToLesson(td: Element): LessonEntity =
        td.html()
            .split("<br>")
            .takeIf { it.size == 3 }
            ?.let {
                Lesson(
                    it.get(0)
                        .substring(3, it.get(0).length - 4)
                        .trim { it <= ' ' },
                    it.get(1),
                    it.get(2)
                )
            } ?: EmptyLesson()

    private fun composeSchedule(
        lessons: Array<Array<LessonEntity>>,
        name: String,
        trArray: Array<Elements>
    ): Array<Schedule> =
        // length of array is number of day in schedule.
        trArray[0].mapIndexed { ind, el ->
            Schedule(
                el.text(),
                name,
                lessons[ind]
            )
        }.toTypedArray()
}