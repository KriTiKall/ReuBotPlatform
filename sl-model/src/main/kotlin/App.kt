import data.entites.HashWrapper
import kotlinx.serialization.encodeToString
import model.entity.*

fun main(args: Array<String>) {
//
//
//    val first = Lesson(
//        "First test2",
//        "Учитель G35ap",
//        "пркатика",
//        "202"
//    )
//
//    val second = Lesson(
//        "Second test2",
//        "Учитель G35ap",
//        "пркатика",
//        "204"
//    )
//    val pair = PairLesson(Pair(first, second))
//    val single = SingleLesson(first)
//
//    println(toJson(single))
//    println(toJson(pair))

    val ar = listOf<String>("asd", "Asdas", "Asdasd", "ASda")
    print(ar.joinToString())
}

fun <Lesson : Indivisible> toJson(lesson: Lesson): String {
    val wrapper = HashWrapper(lesson, lesson.hashCode())
    val json = format.encodeToString(wrapper)
    return json
}
