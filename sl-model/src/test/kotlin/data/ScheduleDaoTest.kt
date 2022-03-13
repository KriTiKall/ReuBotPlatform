package data

import com.example.model.entity.*
import com.example.view.ScheduleOperationDoa
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ScheduleDaoTest {

    private val dao = ScheduleDao()
    private val get = ScheduleOperationDoa()
    private val list = mutableListOf<Schedule>()

    @BeforeAll
    fun initSchedules() {
        list.add(
            Schedule(
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
        )
        list.add(
            Schedule(
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
                    SingleLesson(
                        Lesson(
                            "TestLesson6",
                            "TestTeacher6",
                            "Practic",
                            "100"
                        )
                    ),
                    PairLesson(
                        Pair(
                            EmptyLesson(),
                            Lesson(
                                "TestLesson7",
                                "TestTeacher7",
                                "Practic",
                                "100"
                            )
                        )
                    ),
                    PairLesson(
                        Pair(
                            Lesson(
                                "TestLesson8",
                                "TestTeacher8",
                                "Practic",
                                "100"
                            ),
                            EmptyLesson()
                        )
                    ),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson())
                )
            )
        )
        list.add(
            Schedule(
                "TEST", "13.03.22", arrayOf(
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                    SingleLesson(EmptyLesson()),
                )
            )
        )

    }

    @AfterAll
    fun destroy() {
        dao.destroy()
        get.destroy()
    }

    @Test
    fun saveOrUpdateTest1() {
        dao.saveOrUpdate(list.get(0))
        val getSch = get.getCurrentSchedule("TEST")
//        println("${encode(list.get(0))} \n ${encode(getSch)}")
        assertEquals(list.get(0), getSch)
    }

    @Test
    fun saveOrUpdateTest2() {
        dao.saveOrUpdate(list.get(1))
        val getSch = get.getCurrentSchedule("TEST")
//        println("${encode(list.get(0))} \n ${encode(getSch)}")
        assertEquals(list.get(1), getSch)
    }

    @Test
    fun saveOrUpdateTest3() {
        dao.saveOrUpdate(list.get(2))
        val getSch = get.getCurrentSchedule("TEST")
        println("${encode(list.get(0))} \n ${encode(getSch)}")
        assertEquals(list.get(2), getSch)
    }

    fun encode(schedule: Schedule) : String {
        return format.encodeToString(schedule)
    }
}