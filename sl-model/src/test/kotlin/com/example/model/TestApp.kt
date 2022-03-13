package com.example.model

import com.example.model.entity.Schedule
import com.example.model.parser.ScheduleParser
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {

    val exec = Executors.newSingleThreadScheduledExecutor()
    exec.scheduleAtFixedRate(
        ScheduleReader(ScheduleParser(), TestScheduleService())
    , 0, 1, TimeUnit.MINUTES)
    print("Start")
}


class TestSendIfoScheduleService : IScheduleService {

    private lateinit var first: Schedule

    override fun saveOrUpdate(schedule: Schedule): Boolean {
        var status = ""
        var count = schedule
        var msg = ""

        if (first == null) {
            msg = "First Schedule has $count members"
        } else {
            msg = "First Schedule has ${first} members. Second has "
        }
        println(msg)
        return true
    }
}

class TestScheduleService : IScheduleService {
    override fun saveOrUpdate(schedule: Schedule): Boolean {
        println(schedule)
        return true
    }
}