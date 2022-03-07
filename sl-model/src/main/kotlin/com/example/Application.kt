package com.example

import com.example.model.entity.Schedule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.tomcat.*
import kotlinx.serialization.json.Json
import com.example.model.entity.jsonStrict
import com.example.view.ScheduleOperation
import com.example.view.ScheduleOperationTest
import com.example.view.scheduleOperations

fun main(args: Array<String>): Unit = EngineMain.main(args)

//fun main() {
////    format.decodeFromString<CommonRequestParam>("{\"name\" : \"Hell\"}")
////    ScheduleOperation().getSchedules("asda")
//
//}


fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = jsonStrict
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/api") {
            scheduleOperations(ScheduleOperationTest())
        }
    }
}