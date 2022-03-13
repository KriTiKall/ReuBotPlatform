package com.example

import com.example.model.ScheduleReader
import com.example.model.entity.Schedule
import com.example.model.entity.format
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.tomcat.*
import kotlinx.serialization.json.Json
import com.example.model.entity.jsonStrict
import com.example.model.parser.ScheduleParser
import com.example.view.ScheduleOperation
import com.example.view.ScheduleOperationTest
import com.example.view.scheduleOperations
import data.ScheduleReaderService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit {
    val exec = Executors.newSingleThreadScheduledExecutor()
    exec.scheduleAtFixedRate(
        ScheduleReader(ScheduleParser(), ScheduleReaderService())
        , 0, 1, TimeUnit.MINUTES)
    EngineMain.main(args)
}

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json(format)
    }
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/api") {
            scheduleOperations(ScheduleOperation())
        }
    }
}