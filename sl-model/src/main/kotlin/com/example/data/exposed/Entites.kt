package com.example.data.exposed

import com.example.data.ConnectionGetter
import com.example.data.PropertyReader
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import org.jetbrains.exposed.sql.transactions.transaction

object GroupNamesEntities : Table("model.group_names") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 10).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object SchedulesEntities : Table("model.schedules") {
    val id = integer("id").autoIncrement()
    val nameId = reference("name_id", GroupNamesEntities.id)
    val date = date("date").uniqueIndex()
    val hash = integer("hash")

    override val primaryKey = PrimaryKey(id)
}

object DisciplinesEntities : Table("model.disciplines") {
    val id = integer("id").autoIncrement()
    val name = text("name").uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object TeacherPostsEntities : Table("model.teacher_posts") {
    val id = integer("id").autoIncrement()
    val name = text("name").uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object TeachersEntities : Table("model.teachers") {
    val id = integer("id").autoIncrement()
    val postId = reference("post_id", TeacherPostsEntities.id)
    val name = varchar("name", 100).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

object LessonsEntities : Table("model.lessons") {
    val id = integer("id").autoIncrement()
    val disciplineId = reference("discipline", DisciplinesEntities.id)
    val teacherId = reference("teacher", TeachersEntities.id)
    val type = varchar("type", 25)
    val auditorium = varchar("auditorium", 20)

    override val primaryKey = PrimaryKey(id)
}

object PairLessonEntities : Table("model.pair_lesson") {
    val id = integer("id").autoIncrement()
    val first = integer("first_lesson_id").nullable()
    val second = integer("second_lesson_id").nullable()

    override val primaryKey = PrimaryKey(id)
}

object LessonsToSchedulesEntities : Table("model.lessons_to_schedules") {
    val scheduleId = reference("schedule_id", SchedulesEntities.id).uniqueIndex()
    val position = integer("position").uniqueIndex()
    val lessonRef = integer("lesson_ref_id")
    val hash = integer("hash")
    val updateTime = timestampWithTimeZone("update_time")
    val isSingle = bool("is_single").default(true)
    val isActual = bool("is_actual").default(true).uniqueIndex()
}