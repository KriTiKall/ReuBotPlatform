package data.dao

import model.entity.Schedule

interface IScheduleDao {

    fun save(schedule: Schedule)

    fun getHash(groupName: String, date: String): Int?

    fun update(schedule: Schedule)
}

interface DAO<Entity, Key> {
    fun create(entity: Entity)
    fun read(key: Key): Entity
    fun update(entity: Entity)
    fun delete(entity: Entity)
    val list: List<Entity>?
}