package data.entites

import com.example.model.entity.Indivisible
import kotlinx.serialization.Serializable

@Serializable
data class HashWrapper (val lesson: Indivisible, val hash: Int)