package model

import data.dao.*
import model.entity.Lesson
import model.entity.Schedule
import org.hibernate.SessionFactory
import view.formatterInSchedule
import java.time.LocalDate

class ScheduleSaveService(
    sessionFactory: SessionFactory
): IScheduleService {

    private val scheduleDao = SchedulesDao(sessionFactory)
    private val disciplinesDao = DisciplinesDao(sessionFactory)
    private val teacherPostsDao = TeacherPostsDao(sessionFactory)
    private val teachersDao = TeachersDao(sessionFactory)
    private val lessonsDao = LessonsDao(sessionFactory)

//    private val scheduleUpdatesDao = ScheduleUpdatesDao(sessionFactory) after

    private fun save(schedule: Schedule) {
        schedule.apply {
            val sch = Schedules(
                name = groupName,
                date = LocalDate.parse(date, formatterInSchedule)
            )

            scheduleDao.create(sch)

//            lessons.forEachIndexed{inx, les ->
//                disciplinesDao.getEntityByName(les.n)
//
//                fun save(lesson: Lesson) {
//                    lessonsDao.create(Lessons(
//                        schedule = sch,
//                        discipline =
//                    ))
//                }
//            }
        }
    }

    private fun update(schedule: Schedule) {

    }


    override fun saveOrUpdate(schedule: Schedule) {
        val hash = scheduleDao.getHash(schedule.groupName, schedule.date)
        if (hash == null)
            save(schedule)
        else {
            if (hash != schedule.hashCode()) {
                update(schedule)
            }
        }
    }
}

class LessonService(sessionFactory: SessionFactory) {

    private val disciplinesDao = DisciplinesDao(sessionFactory)
    private val teacherPostsDao = TeacherPostsDao(sessionFactory)
    private val teachersDao = TeachersDao(sessionFactory)
    private val lessonsDao = LessonsDao(sessionFactory)

    fun save(lesson: Lesson, schedules: Schedules) {



//        lessonsDao.create(Lessons(
//            schedule = schedules,
//            discipline =
//        ))
    }

}