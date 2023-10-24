package com.example.data.exposed.dao

import com.example.data.ConnectionGetter
import com.example.data.exposed.*
import com.example.model.entity.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.FieldPosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SchedulesSelectorDao {

    private val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")
    private val logger = StdOutSqlLogger

    private val connection: Database by lazy {
        Database.connect({ ConnectionGetter.getConnection() })
    }

    fun findAllBy(groupName: String): List<Schedule> {
        val list = mutableListOf<Schedule>()
        transaction(connection) {
            addLogger(logger)

            (SchedulesEntities fullJoin GroupNamesEntities)
                .select { GroupNamesEntities.name eq groupName }
                .forEach {
                    list.add(
                        Schedule(
                            it[GroupNamesEntities.name],
                            it[SchedulesEntities.date].format(formatterInSchedule),
                            findSchLessons(it[SchedulesEntities.id])
                        )
                    )
                }

        }
        return list
    }

    fun findBy(groupName: String, date: String): Schedule {
        var data = Schedule()
        transaction(connection) {
            addLogger(logger)

            data = (SchedulesEntities fullJoin GroupNamesEntities)
                .select { (GroupNamesEntities.name eq groupName) and (SchedulesEntities.date eq LocalDate.parse(date)) }
                .first()
                .let {
                    Schedule(
                        it[GroupNamesEntities.name],
                        it[SchedulesEntities.date].format(formatterInSchedule),
                        findSchLessons(it[SchedulesEntities.id])
                    )
                }

        }
        return data
    }

    fun findSchLessons(schId: Int): Array<Indivisible> {
        val ar: Array<Indivisible> = Array(8) { SingleLesson(EmptyLesson()) }

        transaction(connection) {
            addLogger(logger)

            LessonsToSchedulesEntities.select { LessonsToSchedulesEntities.scheduleId eq schId }
                .forEach { lts ->
                    if (lts[LessonsToSchedulesEntities.isSingle]) {
                        ar[lts[LessonsToSchedulesEntities.position]] = SingleLesson(
                            selectLesson(lts[LessonsToSchedulesEntities.lessonRef])
                        )
                    } else {
                        val pair =
                            PairLessonEntities.select { PairLessonEntities.id eq lts[LessonsToSchedulesEntities.lessonRef] }
                                .first()
                                .let { it[PairLessonEntities.first]!! to it[PairLessonEntities.second]!! }

                        ar[lts[LessonsToSchedulesEntities.position]] = PairLesson(
                            selectLesson(pair.first) to selectLesson(pair.second)
                        )
                    }
                }
        }
        return ar
    }

    fun findCellBy(groupName: String, date: String, position: Int): Indivisible {
        val schId = (SchedulesEntities leftJoin DisciplinesEntities)
            .select { (GroupNamesEntities.name eq groupName) and (SchedulesEntities.date eq LocalDate.parse(date)) }
            .first()
            .let { it[SchedulesEntities.id] }

        return LessonsToSchedulesEntities.select { (LessonsToSchedulesEntities.scheduleId eq schId) and (LessonsToSchedulesEntities.position eq position) }
            .first()
            .let { lts ->
                if (lts[LessonsToSchedulesEntities.isSingle]) {
                    SingleLesson(
                        selectLesson(lts[LessonsToSchedulesEntities.lessonRef])
                    )
                } else {
                    val pair =
                        PairLessonEntities.select { PairLessonEntities.id eq lts[LessonsToSchedulesEntities.lessonRef] }
                            .first()
                            .let { it[PairLessonEntities.first]!! to it[PairLessonEntities.second]!! }

                    PairLesson(
                        selectLesson(pair.first) to selectLesson(pair.second)
                    )
                }
            }
    }

    private fun selectLesson(lesId: Int): Lesson =
        (LessonsEntities fullJoin DisciplinesEntities
                fullJoin TeachersEntities
                fullJoin TeacherPostsEntities)
            .select { LessonsEntities.id eq lesId }
            .first()
            .let { row ->
                Lesson(
                    row[DisciplinesEntities.name],
                    row[TeacherPostsEntities.name],
                    row[TeachersEntities.name],
                    row[LessonsEntities.type],
                    row[LessonsEntities.auditorium]
                )
            }
}