package data

import data.entites.ScheduleDbMapper
import model.MockScheduleService
import model.ScheduleReader
import model.parser.ScheduleParser
import java.sql.DriverManager
import java.sql.SQLType
import java.sql.Types
import java.time.LocalDate

fun main() {

    val url = "jdbc:postgresql://localhost:5432/rea-bot-api-db?user=bot-api-admin&password=admin"

    val schedule = ScheduleReader(ScheduleParser(true), MockScheduleService()).parseSchedule(LocalDate.now())

    val mapper = ScheduleDbMapper().set(schedule[0])

    val con = DriverManager.getConnection(url).use {

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

        val stm = it.prepareStatement("select * from model.get_lesson_ref(?::jsonb)")
        stm.setString(1, mapper.lessonJsonb[2])
        val set = stm.executeQuery()
        set.next()
        print("${set.getInt(1)} ${set.getBoolean(2)} ${set.getBoolean(3)}")

//                val stm = it.prepareStatement("select * from cal(?)")
//        stm.setInt(1, 123)
//        val set = stm.executeQuery()
//        set.next()
//
//        print("${set.getInt(1)}")


    }
}