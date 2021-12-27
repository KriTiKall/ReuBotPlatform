package view

import model.entity.Lesson
import model.entity.Schedule
import java.time.LocalDate

interface IScheduleOperations {

    fun getCurrentSchedule(groupName: String): Schedule
    fun getSchedules(groupName: String): Array<Schedule>
    fun getNextLesson(groupName: String): Lesson
    fun getSchedule(groupName: String, date: LocalDate): Schedule
}

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

    override fun getSchedule(groupName: String, date: LocalDate): Schedule {
        TODO("Not yet implemented")
    }
}