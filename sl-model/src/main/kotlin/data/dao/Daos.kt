package data.dao

import org.hibernate.SessionFactory

class SchedulesDao(sessionFactory: SessionFactory) :
    AbstractDao<Schedules, Long>(sessionFactory, Schedules())

class DisciplinesDao(sessionFactory: SessionFactory) :
    AbstractDao<Disciplines, String>(sessionFactory, Disciplines())

class TeacherPostsDao(sessionFactory: SessionFactory) :
    AbstractDao<TeacherPosts, String>(sessionFactory, TeacherPosts())

class TeachersDao(sessionFactory: SessionFactory) :
    AbstractDao<Teachers, String>(sessionFactory, Teachers())

class LessonsDao(sessionFactory: SessionFactory) :
    AbstractDao<Lessons, String>(sessionFactory, Lessons())

class ScheduleUpdatesDao(sessionFactory: SessionFactory) :
    AbstractDao<ScheduleUpdates, String>(sessionFactory, ScheduleUpdates())