
import io.ktor.application.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import model.entity.EmptyLesson
import model.entity.Lesson
import model.entity.LessonEntity
import model.entity.PairLesson
import view.ScheduleOperation
import view.scheduleOperations

val module = SerializersModule {
    polymorphic(LessonEntity::class) {
        subclass(EmptyLesson::class)
        subclass(Lesson::class)
        subclass(PairLesson::class)
    }
}

val format = Json { serializersModule = module }

fun main(args: Array<String>) {
//    val op = ScheduleOperation()
//
//    op.getCurrentSchedule("dsf")

//    EngineMain.main(args)
}

fun Application.module(testing: Boolean = false) {
    val operationService = ScheduleOperation()
    routing {
        this.scheduleOperations(operationService)
    }
}