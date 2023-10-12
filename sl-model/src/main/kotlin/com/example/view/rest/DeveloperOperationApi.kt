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
    route("/exists") {
        // GET /api/dev/schedules  { "name" : "ПКо-41" }
        get("/groups") {
            val param = call.receive<CommonRequestParam>()

            try {
                call.respond(service.getExistsGroups(param.date))
            } catch (e: Exception) {
                call.respondText(
                    "Error: ${e.printStackTrace()}",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        get("/dates") {
            val param = call.receive<CommonRequestParam>()

            try {
                call.respond(service.getExistsDates(param.name))
            } catch (e: Exception) {
                call.respondText(
                    "Error: ${e.printStackTrace()}",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
    }

}