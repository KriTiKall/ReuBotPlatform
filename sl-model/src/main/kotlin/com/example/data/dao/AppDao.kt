package data.dao

import com.example.data.ConnectionGetter
import com.example.data.PropertyReader
import com.example.model.MockScheduleService
import com.example.model.ScheduleReader
import com.example.model.parser.ScheduleParser
import data.entites.ScheduleDbMapper
import java.time.LocalDate

fun main() {

    PropertyReader.load()
    val getter = ConnectionGetter

    val schedule = ScheduleReader(ScheduleParser(true), MockScheduleService()).parseSchedule(LocalDate.now())

    val mapper = ScheduleDbMapper().set(schedule[0])

    val con = getter.getConnection().use {

//        val call = it.prepareCall("{select cal(?)}")
//        call.setInt(1, 12)
//        call.registerOutParameter(2, Types.INTEGER)
//        call.execute()
//
//        println(call.getInt(1))


//        val call = it.prepareCall("{call model.get_lesson_ref(?, ?, ?, ?)}")
//        call.setString(1, mapper.toString())
//        call.registerOutParameter(2, Types.INTEGER)
//        call.registerOutParameter(3, Types.BOOLEAN)
//        call.registerOutParameter(4, Types.BOOLEAN)
//        call.execute()
//
//        println(call.getInt(1))
//        println(call.getBoolean(2))
//        println(call.getBoolean(3))

        val stm = it.prepareStatement("select current_time")
        val resultSet = stm.executeQuery()
        resultSet.next()
        println(resultSet.getString(1))
//        val stm = it.prepareStatement("select * from model.get_lesson_ref(?::jsonb)")
//        stm.setString(1, mapper.lessonJsonb[2])
//        val set = stm.executeQuery()
//        set.next()
//        print("${set.getInt(1)} ${set.getBoolean(2)} ${set.getBoolean(3)}")

//                val stm = it.prepareStatement("select * from cal(?)")
//        stm.setInt(1, 123)
//        val set = stm.executeQuery()
//        set.next()
//
//        print("${set.getInt(1)}")
    }
}