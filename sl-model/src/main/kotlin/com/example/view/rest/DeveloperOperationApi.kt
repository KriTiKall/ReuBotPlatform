package com.example.view

import com.example.view.services.IDeveloperOperations
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable


fun Route.developerOperations(service: IDeveloperOperations) {

    // GET /api/dev/groups
    get("/groups") {
        try {
            call.respond(service.getGroupNames())
        } catch (e: Exception) {
            call.respondText(
                "Error: ${e.printStackTrace()}",
                status = HttpStatusCode.BadRequest
            )
        }
    }

    // GET /api/dev/teachers  { "name" : "ПКо-41" }
    get("/teachers") {
        try {
            call.respond(service.getTeacherNames())
        } catch (e: Exception) {
            call.respondText(
                "Error: ${e.printStackTrace()}",
                status = HttpStatusCode.BadRequest
            )
        }
    }

    // GET /api/dev/schedules  { "name" : "ПКо-41" }
    get("/schedules") {
        val param = call.receive<DevRequestParam>()

        try {
            call.respond(service.getSchedules(param.date))
        } catch (e: Exception) {
            call.respondText(
                "Error: ${e.printStackTrace()}",
                status = HttpStatusCode.BadRequest
            )
        }
    }
}

@Serializable
data class DevRequestParam(val date: String = "");