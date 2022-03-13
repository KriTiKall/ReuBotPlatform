package data.entites

import kotlinx.serialization.Serializable
import model.entity.Indivisible

@Serializable
data class HashWrapper (val lesson: Indivisible, val hash: Int)