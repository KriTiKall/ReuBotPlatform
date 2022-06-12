import com.example.model.entity.*
import com.example.view.ScheduleOperationDoa
import data.entites.HashWrapper
import kotlinx.serialization.encodeToString


fun main(args: Array<String>) {
    val get = ScheduleOperationDoa()
    print(format.encodeToString(get.getSchedule("БДо-11", "2022-06-14",false)))
}

fun <Lesson : Indivisible> toJson(lesson: Lesson): String {
    val wrapper = HashWrapper(lesson, lesson.hashCode())
    val json = format.encodeToString(wrapper)
    return json
}
