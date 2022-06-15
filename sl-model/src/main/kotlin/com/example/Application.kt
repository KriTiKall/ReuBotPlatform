package com.example

import com.example.data.PropertyReader
import com.example.model.ScheduleReader
import com.example.model.entity.format
import com.example.model.parser.ScheduleParser
import com.example.view.ScheduleOperation
import com.example.view.scheduleOperations
import data.dao.ScheduleReaderService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.tomcat.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit {
    PropertyReader.load()
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
            route("/dev") {
                get("/") {
                    call.respondText("Hello, dev")
                }

            }
        }
    }
}