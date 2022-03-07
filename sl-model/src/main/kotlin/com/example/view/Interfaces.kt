package com.example.view

import com.example.model.entity.Indivisible
import com.example.model.entity.Lesson
import com.example.model.entity.Schedule

interface IScheduleOperations {

    fun getCurrentSchedule(groupName: String): Schedule
    fun getSchedules(groupName: String): Array<Schedule>
    fun getNextLesson(groupName: String): Indivisible
    fun getSchedule(groupName: String, date: String): Schedule
}