package data.dao

import org.hibernate.SessionFactory
import javax.persistence.Query

class SchedulesDao(sessionFactory: SessionFactory) :
    AbstractDao<Schedules, Long>(sessionFactory, Schedules()) {

    fun getHash(groupName: String, date: String): Int? {
            sessionFactory.openSession().use { session ->
                val query: Query = session.createQuery("select s.hash from Schedules s where name = :name and date = :date")
                query.setParameter("name", groupName)
                query.setParameter("date", date)
                return query.resultList[0] as Int?
            }
        }
    }

class DisciplinesDao(sessionFactory: SessionFactory) :
    AbstractDao<Disciplines, String>(sessionFactory, Disciplines()) {

        fun getEntityByName(name: String): Disciplines? {
            sessionFactory.openSession().use { session ->
                val query: Query = session.createQuery("from Disciplines where name = :name")
                query.setParameter("name",name)
                return query.firstResult as Disciplines?
            }
        }
    }

class TeacherPostsDao(sessionFactory: SessionFactory) :
    AbstractDao<TeacherPosts, String>(sessionFactory, TeacherPosts()) {

    fun getEntityByName(name: String): TeacherPosts? {
        sessionFactory.openSession().use { session ->
            val query: Query = session.createQuery("from TeacherPosts where name = :name")
            query.setParameter("name",name)
            return query.firstResult as TeacherPosts?
        }
    }
}

class TeachersDao(sessionFactory: SessionFactory) :
    AbstractDao<Teachers, String>(sessionFactory, Teachers()) {

    fun getEntityByName(name: String): Teachers? {
        sessionFactory.openSession().use { session ->
            val query: Query = session.createQuery("from Teachers where name = :name")
            query.setParameter("name",name)
            return query.firstResult as Teachers?
        }
    }
}

class LessonsDao(sessionFactory: SessionFactory) :
    AbstractDao<Lessons, String>(sessionFactory, Lessons())

class ScheduleUpdatesDao(sessionFactory: SessionFactory) :
    AbstractDao<ScheduleUpdates, String>(sessionFactory, ScheduleUpdates())