package data.dao

import com.example.data.ConnectionGetter
import com.example.model.IScheduleService
import com.example.model.entity.Schedule
import data.entites.ScheduleDbMapper

class ScheduleReaderService : IScheduleService {

    private val dao = ScheduleDao()

    override fun saveOrUpdate(schedule: Schedule): String {
        return dao.saveOrUpdate(schedule)
    }
}

class ScheduleDao  {

    private val connection = ConnectionGetter.getConnection()

    fun destroy() {
        connection.close()
    }

    fun saveOrUpdate(schedule: Schedule): String {
        val mapper = ScheduleDbMapper().set(schedule)
        val stm = connection.prepareStatement("select * from model.insert_or_update_schedule($mapper::model.schedule)")
        val set = stm.executeQuery()
        set.next()
        val action = set.getString(2)
        println("${set.getString(1)}")
        return action
    }
}