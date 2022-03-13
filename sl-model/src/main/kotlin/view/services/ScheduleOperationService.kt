package view

import data.entites.ScheduleDbMapper
import kotlinx.serialization.decodeFromString
import model.entity.Lesson
import model.entity.Schedule
import model.entity.format
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate

class ScheduleOperation : IScheduleOperations {

    override fun getCurrentSchedule(groupName: String): Schedule {
        TODO("Not yet implemented")
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        TODO("Not yet implemented")
    }

    override fun getNextLesson(groupName: String): Lesson {
        TODO("Not yet implemented")
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        TODO("Not yet implemented")
    }
}

class ScheduleOperationDoa : IScheduleOperationsDao {

    private val connection: Connection

    init {
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