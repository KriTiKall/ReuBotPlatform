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