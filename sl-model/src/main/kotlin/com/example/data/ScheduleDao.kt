package data

import com.example.data.ScheduleReaderDAO
import com.example.model.IScheduleService
import com.example.model.entity.Schedule
import data.entites.ScheduleDbMapper
import java.sql.Connection
import java.sql.DriverManager

class ScheduleReaderService : IScheduleService {

    private val dao = ScheduleDao()

    override fun saveOrUpdate(schedule: Schedule): Boolean {
        return dao.saveOrUpdate(schedule)
    }
}

class ScheduleDao : ScheduleReaderDAO {

    private val connection: Connection

    init {
        val url = "jdbc:postgresql://localhost:5432/rea-bot-api-db?user=bot-api-admin&password=admin"
        connection = DriverManager.getConnection(url)
    }

    fun destroy() {
        connection.close()
    }

    override fun saveOrUpdate(schedule: Schedule): Boolean {
        val mapper = ScheduleDbMapper().set(schedule)
        val stm = connection.prepareStatement("select * from model.insert_or_update_schedule($mapper::model.schedule)")
        val set = stm.executeQuery()
        set.next()
        val action = set.getString(2)
        println("${set.getString(1)} $action")
        return action == "insert"
    }
}