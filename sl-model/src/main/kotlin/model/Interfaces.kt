package model

import model.entity.Lesson
import model.entity.Schedule
import java.util.*

interface ScheduleOperations {

     fun getCurrentSchedule(groupName: String): Schedule
     fun getSchedules(groupName: String?): Array<Schedule>
     fun getNextLesson(groupName: String): Lesson
     fun getSchedule(groupName: String, date: Date): Schedule
}