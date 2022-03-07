package com.example.view

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter

val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")


fun Route.scheduleOperations(service: IScheduleOperations) {

    // GET /api/schedule/current  { "name" : "ПКо-41" }
    get("/schedule/current") {
        val param = call.receive<CommonRequestParam>()

        if (param.name.isNotBlank()) {
            call.respond(service.getCurrentSchedule(param.name))
        } else {
            call.respondText("Parameter 'name' is blank", status = HttpStatusCode.BadRequest)
        }
    }

    // GET /api/schedule  { "name" : "ПКо-41" }
    get("/schedules") {
        val param = call.receive<CommonRequestParam>()

        if (param.name.isNotBlank()) {
            call.respond(service.getSchedules(param.name))
        } else {
            call.respondText("Parameter 'name' is blank", status = HttpStatusCode.BadRequest)
        }
    }

    // GET /api/lesson/next  { "name" : "ПКо-41" }
    get("/lesson/next") {
        val param = call.receive<CommonRequestParam>()

        if (param.name.isNotBlank()) {
            call.respond(service.getNextLesson(param.name))
        } else {
            call.respondText("Parameter 'name' is blank", status = HttpStatusCode.BadRequest)
        }
    }

    // GET /schedule  { "name" : "ПКо-41", "date" : "28.12.2021" }
    get("/schedule") {
        val param = call.receive<CommonRequestParam>()

        if (param.name.isNotBlank() && param.date.isNotBlank()) {
            call.respond(service.getSchedule(param.name, param.date))
        } else {
            call.respondText("Parameter 'name' or 'date' is blank", status = HttpStatusCode.BadRequest)
        }
    }
}

@Serializable
data class CommonRequestParam(val name: String = "", val date: String = "");