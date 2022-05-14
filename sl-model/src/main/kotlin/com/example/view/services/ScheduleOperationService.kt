package com.example.view

import com.example.model.entity.*
import kotlinx.serialization.decodeFromString
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate

class ScheduleOperation : IScheduleOperations {

    private val dao = ScheduleOperationDoa();

    override fun getCurrentSchedule(groupName: String): Schedule {
        return dao.getCurrentSchedule(groupName)
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        TODO("Not yet implemented")
    }

    override fun getNextLesson(groupName: String): Indivisible {
        TODO("Not yet implemented")
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        return dao.getSchedule(groupName, date)
    }
}

class ScheduleOperationTest : IScheduleOperations {

    override fun getCurrentSchedule(groupName: String): Schedule {
        return Schedule()
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        return arrayOf(Schedule())
    }

    override fun getNextLesson(groupName: String): Indivisible {
        return SingleLesson(EmptyLesson())
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        return Schedule()
    }
}

class ScheduleOperationDoa : IScheduleOperationsDao {

    private val connection: Connection

    init { //TODO move to another class
        val url = "jdbc:postgresql://localhost:5432/rea-bot-api-db?user=bot-api-admin&password=admin"
        connection = DriverManager.getConnection(url)
    }

    fun destroy() {
        connection.close()
    }

    override fun getCurrentSchedule(groupName: String): Schedule {
        return getSchedule(groupName, LocalDate.now().toString())
    }

    override fun getSchedules(groupName: String): Array<Schedule?> {
        TODO("Not yet implemented")
    }

    override fun getNextLesson(groupName: String): Lesson? {
        TODO("Not yet implemented")
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        val stm = connection.prepareStatement("select * from model.get_schedule('$groupName'::varchar, '$date'::date, 'true');" )
        val set = stm.executeQuery()
        set.next()
        val json = set.getString(1)
        val isEmpty = set.getBoolean(2)
        println("${set.getString(3)}")
        return if (!isEmpty)
            format.decodeFromString(json)
        else
            Schedule()
    }
}