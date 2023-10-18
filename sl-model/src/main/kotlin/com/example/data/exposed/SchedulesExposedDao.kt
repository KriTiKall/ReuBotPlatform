package com.example.data.exposed

import com.example.data.ConnectionGetter
import com.example.model.entity.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.OffsetDateTime

class SchedulesExposedDao() {

    private val connection: Database

    init {
        connection = Database.connect({ ConnectionGetter.getConnection() })
    }

    fun insertSchedule(schedule: Schedule) {
        transaction(connection) {
            addLogger(StdOutSqlLogger)

            val groupId = getIdOrInsert(
                GroupNamesEntities,
                GroupNamesEntities.name,
                schedule.groupName,
                GroupNamesEntities.id
            )

            val schId = SchedulesEntities.insert {
                it[nameId] = groupId
                it[hash] = schedule.hashCode()
                it[date] = LocalDate.parse(schedule.date)
            } get SchedulesEntities.id

            schedule.lessons.forEachIndexed { i, ind ->
                saveScheduleLesson(ind, schId, i)
            }
        }
    }

    fun saveScheduleLesson(lessonCell: Indivisible, scheduleId: Int, index: Int) {
        val (id, isSing) = insertLessonCell(lessonCell)

        if (id != -1) {
            LessonsToSchedulesEntities.insert {
                it[LessonsToSchedulesEntities.scheduleId] = scheduleId
                it[position] = index
                it[lessonRef] = id
                it[hash] = lessonCell.hashCode()
                it[updateTime] = OffsetDateTime.now()
                it[isSingle] = isSing
                it[isActual] = true
            }
        }
    }

    /** Return pair of value. First is id of lesson cell. Second mean what the table it  referenced.
    true - model.lessons/ false - model.pair_lesson*/
    fun insertLessonCell(lessonCell: Indivisible): Pair<Int, Boolean> {
        var pair = Pair(-1, false)
        transaction(connection) {
            addLogger(StdOutSqlLogger)

            if (lessonCell is SingleLesson) {
                val lesson = lessonCell.lesson

                if (lesson is Lesson)
                    pair = Pair(insertLessonAndGetId(lesson), true)
            }

            if (lessonCell is PairLesson) {
                val (f, s) = lessonCell.pair

                var id1: Int? = null
                var id2: Int? = null

                if (f is Lesson)
                    id1 = insertLessonAndGetId(f)

                if (s is Lesson)
                    id2 = insertLessonAndGetId(s)

                val pairId = PairLessonEntities.insert {
                    it[first] = id1
                    it[second] = id2
                } get PairLessonEntities.id

                pair = pairId to false
            }

        }
        return pair
    }

    private fun insertLessonAndGetId(lesson: Lesson): Int {
        var id: Int = -1
        transaction(connection) {
            addLogger(StdOutSqlLogger)

            val disId = getIdOrInsert(
                DisciplinesEntities,
                DisciplinesEntities.name,
                lesson.name,
                DisciplinesEntities.id
            )

            val postId = getIdOrInsert(
                TeacherPostsEntities,
                TeacherPostsEntities.name,
                lesson.teacherPost,
                TeacherPostsEntities.id
            )

            val teachId = getIdOrInsertPair(
                TeachersEntities,
                Pair(TeachersEntities.name, lesson.teacherName),
                Pair(TeachersEntities.postId, postId),
                TeachersEntities.id
            )

            id = LessonsEntities.insert {
                it[disciplineId] = disId
                it[teacherId] = teachId
                it[type] = lesson.type
                it[auditorium] = lesson.auditorium
            } get LessonsEntities.id
        }
        return id;
    }

    private fun getIdOrInsert(
        entity: Table,
        field: Column<String>,
        value: String,
        id: Column<Int>
    ): Int {
        val query = entity.select { field eq value }

        return if (query.empty()) {
            entity.insert {
                it[field] = value
            } get id
        } else
            query.single()[id]
    }

    private fun getIdOrInsertPair(
        entity: Table,
        pairString: Pair<Column<String>, String>,
        pairInt: Pair<Column<Int>, Int>,
        id: Column<Int>
    ): Int {
        val query = entity.select {
            (pairString.first eq pairString.second) and (pairInt.first eq pairInt.second)
        }

        return if (query.empty()) {
            entity.insert {
                it[pairString.first] = pairString.second
                it[pairInt.first] = pairInt.second
            } get id
        } else
            query.single()[id]
    }
}