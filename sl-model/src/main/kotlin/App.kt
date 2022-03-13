import data.ScheduleReaderService
import data.entites.HashWrapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import model.MockScheduleService
import model.ScheduleReader
import model.entity.*
import model.parser.ScheduleParser
import view.ScheduleOperationDoa
import java.time.LocalDate

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
//    val schedule = ScheduleReader(ScheduleParser(true), MockScheduleService()).parseSchedule(LocalDate.now())
//
//    val sch = format.decodeFromString<Schedule>("{\"date\": \"2021-12-28\", \"lessons\": [{\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"name\": \"Иностранный язык\", \"type\": \"Lesson\", \"auditorium\": \"Moodle\", \"lessonType\": \"практика\", \"teacherName\": \"Осколкова В.Р.\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}, {\"type\": \"SingleLesson\", \"lesson\": {\"type\": \"Empty\"}}], \"groupName\": \"Бо-11\"}")
//    println(sch);
//
//    println(format.encodeToString(schedule[0]))
//    println(toJson(pair))

//    val schedule = ScheduleReader(ScheduleParser(true), ScheduleReaderService())
//    schedule.run()

//    val dao = ScheduleOperationDoa()
//    println(dao.getCurrentSchedule("ПРи-11"))
    val sch = Schedule(
        "TEST", "13.03.22", arrayOf(
            SingleLesson(EmptyLesson()),
            SingleLesson(
                Lesson(
                    "TestLesson1",
                    "TestTeacher1",
                    "Practic",
                    "100"
                )
            ),
            PairLesson(
                Pair(
                    Lesson(
                        "TestLesson2",
                        "TestTeacher2",
                        "Practic",
                        "100"
                    ),
                    Lesson(
                        "TestLesson3",
                        "TestTeacher3",
                        "Practic",
                        "100"
                    )
                )
            ),
            SingleLesson(EmptyLesson()),
            PairLesson(
                Pair(
                    Lesson(
                        "TestLesson4",
                        "TestTeacher4",
                        "Practic",
                        "100"
                    ),
                    EmptyLesson()
                )
            ),
            PairLesson(
                Pair(
                    EmptyLesson(),
                    Lesson(
                        "TestLesson5",
                        "TestTeacher5",
                        "Practic",
                        "100"
                    )
                )
            ),
            SingleLesson(EmptyLesson()),
            SingleLesson(EmptyLesson())
        )
    )

    print(format.encodeToString(sch))
}

fun <Lesson : Indivisible> toJson(lesson: Lesson): String {
    val wrapper = HashWrapper(lesson, lesson.hashCode())
    val json = format.encodeToString(wrapper)
    return json
}
