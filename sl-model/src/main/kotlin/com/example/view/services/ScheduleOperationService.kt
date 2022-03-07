package com.example.view

import com.example.model.entity.*

class ScheduleOperation : IScheduleOperations {

    override fun getCurrentSchedule(groupName: String): Schedule {
        TODO("Not yet implemented")
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        TODO("Not yet implemented")
    }

    override fun getNextLesson(groupName: String): Indivisible {
        TODO("Not yet implemented")
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        TODO("Not yet implemented")
    }
}

class ScheduleOperationTest : IScheduleOperations {

    override fun getCurrentSchedule(groupName: String): Schedule {
        return Schedule()
    }

    override fun getSchedules(groupName: String): Array<Schedule> {
        return arrayOf(Schedule())
    }

    override fun getNextLesson(groupName: String): Indivisible {
        return SingleLesson(EmptyLesson())
    }

    override fun getSchedule(groupName: String, date: String): Schedule {
        return Schedule()
    }
}