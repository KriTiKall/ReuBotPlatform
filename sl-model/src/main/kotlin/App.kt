import com.example.model.entity.*
import data.entites.HashWrapper
import kotlinx.serialization.encodeToString

fun main(args: Array<String>) {

}

fun <Lesson : Indivisible> toJson(lesson: Lesson): String {
    val wrapper = HashWrapper(lesson, lesson.hashCode())
    val json = format.encodeToString(wrapper)
    return json
}
