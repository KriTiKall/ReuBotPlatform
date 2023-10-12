package data.dao

import com.example.data.ConnectionGetter
import com.example.data.dao.ScheduleReaderDAO
import com.example.model.IScheduleService
import com.example.model.entity.Schedule
import data.entites.ScheduleDbMapper
import java.sql.Connection

class ScheduleReaderService : IScheduleService {

    private val dao = ScheduleDao()

    override fun saveOrUpdate(schedule: Schedule): Boolean {
        return dao.saveOrUpdate(schedule)
    }
}

class ScheduleDao : ScheduleReaderDAO {

    private val connection: Connection

    init {
        connection = ConnectionGetter.getConnection()
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

        return action == "insert"
    }
}