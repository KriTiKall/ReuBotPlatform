package dao

import model.entity.Schedule
import java.time.LocalDate

interface IScheduleDao {

    fun save(schedule: Schedule)

    fun getHash(groupName: String, date: String): Int?

    fun update(schedule: Schedule)
}