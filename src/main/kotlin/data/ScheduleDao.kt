package data

import model.entity.Schedule

interface IScheduleDao {

    fun update(schedule: Schedule)
    fun save(schedule: Schedule)
    fun getHash(groupName: String, date: String): Int
}

class ScheduleDao {
}