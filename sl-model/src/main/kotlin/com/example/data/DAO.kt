package com.example.data

import com.example.model.entity.Schedule


interface DAO<Entity, Key> {
    fun create(entity: Entity)
    fun read(key: Key): Entity
    fun update(entity: Entity)
    fun delete(entity: Entity)
    val list: List<Entity>?
}

interface ScheduleReaderDAO {

    fun saveOrUpdate(schedule: Schedule): Boolean
}

