
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.ScheduleReader
import model.ScheduleService
import model.entity.EmptyLesson
import model.entity.Lesson
import model.entity.LessonEntity
import model.entity.PairLesson
import model.parser.ScheduleParser
import view.ScheduleOperation
import view.scheduleOperations
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {

    EngineMain.main(args)

    val exec = Executors.newSingleThreadScheduledExecutor()
    exec.scheduleAtFixedRate({
        ScheduleReader(ScheduleParser(), ScheduleService())
    }, 0, 5, TimeUnit.MINUTES)
}

fun Application.module(testing: Boolean = false) {
    val operationService = ScheduleOperation()
    routing {
        this.scheduleOperations(operationService)
    }
}