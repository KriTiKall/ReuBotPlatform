package com.example.view

import com.example.data.ConnectionGetter
import com.example.model.entity.*
import kotlinx.serialization.decodeFromString
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate

class ScheduleOperation : IScheduleOperations {

    private val dao = ScheduleOperationDoa();

    override fun getCurrentSchedule(groupName: String): Schedule {
        return dao.getSchedule(groupName, LocalDate.now().toString(), true)
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        return dao.getSchedules(groupName)
    }

    override fun getNextLesson(groupName: String): Indivisible {
        return dao.getNextLesson(groupName)
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        return dao.getSchedule(groupName, date, true)
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

    init {
        connection = ConnectionGetter.getConnection()
    }

    fun destroy() {
        connection.close()
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        val stm = connection.prepareStatement("select * from model.get_schedules('$groupName'::varchar, 'true');" )
        val set = stm.executeQuery()
        set.next()
        val json = set.getString(1)
        println("${set.getString(2)}")
        return format.decodeFromString(json)
    }

    override fun getNextLesson(groupName: String): Indivisible {
        val stm = connection.prepareStatement("select * from model.get_next_lesson('$groupName');" )
        val set = stm.executeQuery()
        set.next()
        val json = set.getString(1)
        println("${set.getBoolean(2)} : ${set.getString(3)}")
        return format.decodeFromString(json)
    }

    override fun getSchedule(groupName: String, date: String, isActual: Boolean): Schedule {
        val stm = connection.prepareStatement("select * from model.get_schedule('$groupName'::varchar, '$date'::date, '$isActual');" )
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