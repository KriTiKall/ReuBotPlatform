package data

interface DAO<Entity, Key> {
    fun create(entity: Entity)
    fun read(key: Key): Entity
    fun update(entity: Entity)
    fun delete(entity: Entity)
    val list: List<Entity>?
}