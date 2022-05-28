package com.example.model

import com.example.data.BrokerService
import com.example.model.entity.Schedule
import com.example.model.parser.ScheduleParser
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {

    val exec = Executors.newSingleThreadScheduledExecutor()
    exec.scheduleAtFixedRate(
        ScheduleReader(ScheduleParser(), TestScheduleService(), BrokerService())
    , 0, 1, TimeUnit.MINUTES)
    print("Start")
}

class TestScheduleService : IScheduleService {
    override fun saveOrUpdate(schedule: Schedule): String {
        println(schedule)
        return ""
    }
}