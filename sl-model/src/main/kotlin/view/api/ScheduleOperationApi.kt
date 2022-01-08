package view

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import model.entity.Lesson
import model.entity.Schedule
import model.entity.format
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatterInSchedule = DateTimeFormatter.ofPattern("dd.MM.yy")


fun Routing.scheduleOperations(service: IScheduleOperations) {

    // example /schedule/current?name=ПКо-41
    get("/schedule/current") {
        val name = call.request.queryParameters["name"]
        if (name != null) {
//            call.respondText("Wrong parameter. Needs write /schedule/current?name=<group>", status = HttpStatusCode.BadRequest)
        } else {
            val json = format.encodeToString<Schedule>(service.getCurrentSchedule(name!!))

            call.respondText(json, status = HttpStatusCode.OK)
        }
    }

    // example /schedule?name=ПКо-41
    get("/schedules") {
        val name = call.request.queryParameters["name"]
        if (name != null) {
//            call.respondText("Wrong parameter. Needs write /schedules?name=<group>", status = HttpStatusCode.BadRequest)
        } else {
            val json = format.encodeToString<Array<Schedule>>(service.getSchedules(name!!))

            call.respondText(json, status = HttpStatusCode.OK)
        }
    }

    // example /lesson/next?name=ПКо-41
    get("/lesson/next") {
        val name = call.request.queryParameters["name"]
        if (name != null) {
//            call.respondText("Wrong parameter. Needs write /lesson/next?name=<group>", status = HttpStatusCode.BadRequest)
        } else {
            val json = format.encodeToString<Lesson>(service.getNextLesson(name!!))

            call.respondText(json, status = HttpStatusCode.OK)
        }
    }

    // example /schedule?name=ПКо-41&date=28.12.2021
    get("/schedule") {

        val name = call.request.queryParameters["name"]
        val date = LocalDate.parse(call.request.queryParameters["data"], formatterInSchedule)

        if (name != null && date != null) {
//            call.respondText("Wrong parameter. Needs write /schedule?name=<group>>&date=<date>", status = HttpStatusCode.BadRequest)
        } else {
            val json = format.encodeToString<Schedule>(service.getSchedule(name!!, date!!))
            call.respondText(json, status = HttpStatusCode.OK)
        }
    }
}
