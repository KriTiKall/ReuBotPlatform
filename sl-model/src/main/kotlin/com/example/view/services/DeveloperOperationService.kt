package com.example.view.services

import com.example.data.ConnectionGetter
import com.example.model.entity.format
import kotlinx.serialization.decodeFromString
import java.sql.Connection

class DeveloperOperationService : IDeveloperOperations {

    private val connection: Connection

    init {
        connection = ConnectionGetter.getConnection()
    }

    fun destroy() {
        connection.close()
    }

    override fun getGroupNames(): Array<String> {
        val array = mutableListOf<String>()
        val stm = connection.prepareStatement("select name from model.group_names")
        val set = stm.executeQuery()
        while (set.next())
            array.add(set.getString(1))
//            println("${set.getBoolean(2)} : ${set.getString(3)}")
        return array.toTypedArray()
    }

    override fun getTeacherNames(): Array<String> {
        val array = mutableListOf<String>()
        val stm = connection.prepareStatement("select name from model.teachers")
        val set = stm.executeQuery()
        while (set.next())
            array.add(set.getString(1))
//            println("${set.getBoolean(2)} : ${set.getString(3)}")
        return array.toTypedArray()
    }

    override fun getExistsGroups(date: String): Array<String> {
        val array = mutableListOf<String>()
        val stm = connection.prepareStatement("select name from model.schedules sch join model.group_names gn on sch.name_id = gn.id where sch.date = '$date'::date")
        val set = stm.executeQuery()
        while (set.next())
            array.add(set.getString(1))

        return array.toTypedArray()
    }

    override fun getExistsDates(name: String): Array<String> {
        val array = mutableListOf<String>()
        val stm = connection.prepareStatement("select sch.date from model.schedules sch join model.group_names gn on sch.name_id = gn.id where gn.name = '$name'")
        val set = stm.executeQuery()
        while (set.next())
            array.add(set.getString(1))

        return array.toTypedArray()
    }
}

interface IDeveloperOperations {

    fun getGroupNames(): Array<String>
    fun getTeacherNames(): Array<String>
    fun getExistsGroups(date: String): Array<String>
    fun getExistsDates(name: String): Array<String>
}