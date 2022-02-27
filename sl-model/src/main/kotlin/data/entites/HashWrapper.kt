package data.entites

import kotlinx.serialization.Serializable

@Serializable
data class HashWrapper<Type> (val lesson: Type, val hash: Int)